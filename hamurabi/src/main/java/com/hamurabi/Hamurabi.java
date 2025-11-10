package com.hamurabi;               
import java.util.Random;         
import java.util.Scanner;

public class Hamurabi {         
    Random rand = new Random();  
    Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) { 
        new Hamurabi().playGame();
    }
    
    void playGame() {
        int population = 100;
        int bushels = 2800;
        int acresOfLand = 1000;
        int pricePerAcre = 19;  
        int yearOfRule = 1;
        int peopleStarved = 0;
        int bushelsHarvested = 0;
        int bushelsEatenByRats = 0;
        int newImmigrants = 0;

        openingSummary(yearOfRule, population, acresOfLand, bushels, pricePerAcre);

        while (yearOfRule <= 10) {

        int acresBought = askHowManyAcresToBuy(pricePerAcre, bushels);

            if (acresBought > 0) {
                bushels -= acresBought * pricePerAcre;
                acresOfLand += acresBought;
            } else {
                int acresSold = askHowManyAcresToSell(pricePerAcre, acresOfLand);

            if (acresSold > 0) {
                bushels += acresSold * pricePerAcre;
                acresOfLand -= acresSold;
            }
        }
        int grainFed = askHowMuchGrainToFeedPeople(bushels);
            if (grainFed > 0) {
                bushels -= grainFed;
            }
        
        int acresPlanted = askHowManyAcresToPlant(acresOfLand, population, bushels);
            if (acresPlanted > 0) {
                bushels -= acresPlanted * 2;
            }
        
        int deaths = plagueDeaths(population);
        population -= deaths;

        int starvationDeaths = starvationDeaths(population, grainFed);
        population -= starvationDeaths;
        peopleStarved = starvationDeaths;

        if (uprising(population + starvationDeaths, starvationDeaths)) {
            return;
        }
        
        if (starvationDeaths == 0) {
            newImmigrants = immigrants(population, acresOfLand, bushels);
            population += newImmigrants;
        } else {
            System.out.println("O great Hammurabi, no new immigrants have joined you this year.");
        }
        
        int harvestedBushels = harvest(acresPlanted, acresPlanted * 2);
        bushels += harvestedBushels;
        bushelsHarvested = harvestedBushels;
    
        int grainLostToRats = grainEatenByRats(bushels);
        bushels -= grainLostToRats;
        bushelsEatenByRats = grainLostToRats;

        pricePerAcre = newCostOfLand();
        System.out.println("O great Hammurabi, Land now costs " + pricePerAcre + " bushels per acre.");

        printSummary(yearOfRule, population, peopleStarved, newImmigrants, acresOfLand, bushelsHarvested, bushelsEatenByRats, bushels, pricePerAcre);

        yearOfRule ++;
    }
    finalSummary(yearOfRule - 1, population, acresOfLand);
}
    
    int askHowManyAcresToBuy(int price, int bushels) {
        System.out.println("O great Hammurabi, Would you like to purchase land? Y or N");
        String response = scanner.nextLine().trim().toUpperCase();
        
        if (response.equals("Y")) {
            System.out.println("O great Hammurabi, How many acres would you like to purchase?");
            int acresToBuy = scanner.nextInt();
            scanner.nextLine(); // 
            int totalCost = acresToBuy * price;
            
            if (totalCost <= bushels) {
                System.out.println("Purchase successful! You bought " + acresToBuy + " acres for " + totalCost + " bushels.");
                return acresToBuy;
            } else {
                System.out.println("O great Hammurabi, You don't have enough grain! You need " + totalCost + " bushels but only have " + bushels + ".");
                return 0;
            }
        } else {
            System.out.println("O great Hammurabi, No land purchased.");
            return 0;
        }
    }
    
    int askHowManyAcresToSell(int price, int acresOwned) {
        System.out.println("O great Hammurabi, Would you like to sell land? Y or N");
        String response = scanner.nextLine().trim().toUpperCase();
        
        if (response.equals("Y")) {
            System.out.println("O great Hammurabi, How many acres would you like to sell?");
            int acresToSell = scanner.nextInt();
            scanner.nextLine(); 
            
            if (acresToSell <= acresOwned) {
                int totalRevenue = acresToSell * price;
                System.out.println("Sale Successful! You sold " + acresToSell + " acres for " + totalRevenue + " bushels.");
                return acresToSell;
            } else {
                System.out.println("O great Hammurabi, You don't have enough land! You only own " + acresOwned + " acres.");
                return 0;
            }
        } else {
            System.out.println("O great Hammurabi, no land was sold.");
            return 0;
        }
    }

    int askHowMuchGrainToFeedPeople(int bushels) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("O great Hammurabi, How many bushels of grain would you like to feed your people?");
        int grainToFeed = userInput.nextInt();

            if (grainToFeed <= bushels) {
                System.out.println("O great Hammurabi, You will feed your people " + grainToFeed + " bushels of grain.");
                return grainToFeed;
            } else {
                System.out.println("O great Hammurabi, You don't have enough grain! You only have " + bushels + "bushels in storage.");
                return 0;
            }
    }

    int askHowManyAcresToPlant(int acresOwned, int population, int bushels) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("O great Hammurabi,, How many acres would you like to plant?");
        int acresToPlant = userInput.nextInt();

            if (acresToPlant > acresOwned) {
                System.out.println("O great Hammurabi, You don't have enough land! You only own " + acresOwned + " acres.");
                return 0;
            }

            int grainNeeded = acresToPlant * 2;
            if (grainNeeded > bushels) {
                System.out.println("O great Hammurabi, You don't have enough grain to plant that much! You need " + grainNeeded + " bushels but only have " + bushels  + ".");
                return 0;
            }

            int maxAcresWithPeople = population * 10; 
                if (acresToPlant > maxAcresWithPeople) {
                    System.out.println("O great Hammurabi, You don't have enough people to farm that much land! Your " + population + " people can only farm " + maxAcresWithPeople + " acres.");
                    return 0;
                }
            
            System.out.println("O great Hammurabi, You will plant " + acresToPlant + " acres.");
            return acresToPlant;

    }
        int plagueDeaths(int population) {
            int chance = rand.nextInt(100);

            if (chance < 15) {
                int deaths = population / 2;
                System.out.println("O great Hammurabi, A horrible plague ravages your population! " + deaths + " people have died.");
                return deaths;
            } else {
                return 0;
            }
        }

        int starvationDeaths(int population, int bushelsIsFedToPeople) {
            int bushelsNeeded = population * 20;


            if (bushelsIsFedToPeople >= bushelsNeeded) {

                if (bushelsIsFedToPeople > bushelsNeeded) {
                    System.out.println("O great Hammurabi, Your people are well fed and happy!");
                } else {
                    System.out.println("O great Hammurabi, Your people have just enough to survive.");
                }
                return 0;
            } else {
                int peopleFed = bushelsIsFedToPeople / 20;
                int deaths = population - peopleFed;
                System.out.println("O great Hammurabi, hunger has taken it's toll and " + deaths + " people starved to death.");
                return deaths;
            }
        }

        boolean uprising(int population, int howManyPeopleStarved) {
            if (howManyPeopleStarved * 100 > population * 45) {
                System.out.println("O great Hammurabi, Over 45% of your people have starved!");
                System.out.println("O great Hammurabi, The people revolt against your terrible leadership!");
                System.out.println("O great Hammurabi, You have been thrown out of office and the game is over.");
                return true;
            } else {
                return false;
            }
        }
    
        int immigrants(int population, int acresOwned, int grainInStorage) {
        int newImmigrants = (20 * acresOwned + grainInStorage) / (100 * population) + 1;
            System.out.println(newImmigrants + " immigrants came to the city!");
            return newImmigrants;
        }

        int harvest(int acres, int busheUsedAsSeed) {
            int yield = rand.nextInt(6) + 1;
            int totalHarvest = acres * yield;
            System.out.println("O great Hammurabi, The harvest yielded " + yield + " bushels per acre.");
            System.out.println("O great Hammurabi, You harvested " + totalHarvest + " bushels total from " + acres + " acres.");

            return totalHarvest;
        }

        int grainEatenByRats(int bushels) {
            int infestationChance = rand.nextInt(100);
            if (infestationChance < 40) {
                int percentEaten = rand.nextInt(21) + 10;
                int grainEaten = (bushels * percentEaten) / 100;
                System.out.println("O great Hammurabi, A rat infestation has run rampant on your food supply! Rats ate " + percentEaten + " % of your grain (" + grainEaten + " bushels).");
                return grainEaten;
            } else {
                System.out.println("O great Hammurabi, You have no rat problems this year.");
                return 0;
            }
        }

        int newCostOfLand() {
            int newPrice = rand.nextInt(7) + 17;
            return newPrice;
        }

        void printSummary(int year, int population, int peopleStarved, int immigrants, int acresOwned, int bushelsHarvested, int bushelsEatenByRats, int bushelsInStorage, int pricePerAcre) {
            System.out.println("\n========================================");
            System.out.println("YEAR " + year + " SUMMARY");
            System.out.println("========================================");
            System.out.println("Population: " + population);
            System.out.println("People who starved: " + peopleStarved);
            System.out.println("Immigrants who came: " + immigrants);
            System.out.println("Acres of land owned: " + acresOwned);
            System.out.println("Bushels harvested: " + bushelsHarvested);
            System.out.println("Bushels eaten by rats: " + bushelsEatenByRats);
            System.out.println("Bushels in storage: " + bushelsInStorage);
            System.out.println("Price per acre: " + pricePerAcre + " bushels");
            System.out.println("========================================\n");
    }
        void finalSummary(int years, int population, int acresOwned) {
            System.out.println("\n========================================");
            System.out.println("GAME OVER - FINAL SUMMARY");
            System.out.println("========================================");
            System.out.println("You ruled for " + years + " years.");
            System.out.println("Final population: " + population);
            System.out.println("Final acres owned: " + acresOwned);
       
        int acresPerPerson = 0;
            if (population > 0) {
       acresPerPerson = acresOwned / population;
            } else {
       System.out.println("Everyone died. You are the worst ruler in history.");
            }

            System.out.println("Acres per person: " + acresPerPerson);
            System.out.println("\n --- YOUR RATING ---");

            if (acresPerPerson >= 10) {
            System.out.println("EXCELLENT! You are a legendary ruler!");
            System.out.println("Your people prosper under your wise leadership.");
        } else if (acresPerPerson >= 7) {
            System.out.println("GOOD! You are a capable ruler.");
            System.out.println("Your people are content and well-cared for.");
        } else if (acresPerPerson >= 5) {
            System.out.println("FAIR. You did okay, but there's room for improvement.");
            System.out.println("Your people survived, but life was tough.");
        } else {
            System.out.println("POOR. Your rule was a struggle.");
            System.out.println("Your people barely survived under your leadership.");
        }

        System.out.println("========================================");
        }

        void openingSummary(int year, int population, int acresOwned, int bushelsInStorage, int pricePerAcre) {
            System.out.println("\n========================================");
            System.out.println("WELCOME TO HAMMURABI!");
            System.out.println("========================================");
            System.out.println("Congratulations! You are the newest ruler of ancient Sumer,");
            System.out.println("elected for a ten year term of office.");
            System.out.println("Your duties are to dispense food, direct farming,");
            System.out.println("and buy and sell land as needed to support your people.");
            System.out.println("Watch out for rat infestations and the plague!");
            System.out.println("\n--- STARTING CONDITIONS ---");
            System.out.println("Year: " + year);
            System.out.println("Population: " + population);
            System.out.println("Acres of land: " + acresOwned);
            System.out.println("Bushels in storage: " + bushelsInStorage);
            System.out.println("Price per acre: " + pricePerAcre + " bushels");
            System.out.println("========================================\n");
}
    }
