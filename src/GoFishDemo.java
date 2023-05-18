import java.util.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class GoFishDemo {

    public static void main(String[] args) {


        // Singleton Pattern to create game instance

        SingleGoFish object = SingleGoFish.getInstance();
        object.showMessage();    

        // Account Creation
        // Prompt the user for the account name and password
        Scanner scanner = new Scanner(System.in);

        
        //Account section commented out --inactive Database


        //Account class working
        //Account account = new Account();
        //account.saveToDatabase();;

 

        // Factory Pattern to create deck of cards
        //--------------------------------------------------

        CardFactory cardFactory = new CardFactory();

        // Can be modified in case of different card combinations
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        String[] names = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    
        List<Card> deck = new ArrayList<>();
        
        // Card Factory to build Cards
 
        for (String suit : suits) {
            for (String name : names) {
                Card card = cardFactory.createCard(suit, name);
                deck.add(card);
            }
        }

        //BUILDER PATTERN FOR CREATING HANDS

        HandDirector director = new HandDirector();
      
        // Create Hand Builder
        HandBuilder randomBuilder = new RandomHandBuilder();

        // Create playerHand and computerHand
        ArrayList<Card> playerHand = director.buildHand(randomBuilder, deck);
        deck.removeAll(playerHand);

        ArrayList<Card> computerHand = director.buildHand(randomBuilder, deck);
        deck.removeAll(computerHand);
        

       

        //--------------------------------------------------

        // Strategy Pattern for AI difficulty

        // Ask for Difficulty Level
        System.out.print("\nSelect difficulty level:\n1: Easy\n2: Medium\n3: Hard\n\n");
        String difficulty = scanner.nextLine();

        Context context = null;
        if (difficulty.equals("1")) {
            //context refers to strategy pattern
            context = new Context(new EasyAIStrategy());

            System.out.println("Difficulty Set To Easy\n");
            //check both hands have cards
        }
            
        else if (difficulty.equals("2")) {
            context = new Context(new MediumAIStrategy());
            System.out.println("Difficulty Set To Medium\n");

        } 
        
        else if (difficulty.equals("3")) {
            context = new Context(new HardAIStrategy());
            System.out.println("Difficulty Set To Hard\n");
            
        } 
        
        else {
            System.out.println("Invalid difficulty level.");
        }

        // Initialize set variables
        AtomicInteger playerSets = new AtomicInteger(0);
        AtomicInteger computerSets = new AtomicInteger(0);

        while (!deck.isEmpty()) {
            // Have player choose card
            // Initialize arraylist string for card selections
            
            System.out.println("Your Hand:");
            for (Card card : playerHand) {
                System.out.println(card.getName() + " of " + card.getSuit());
            }

            System.out.println(" ");
            System.out.println("---Player's turn:---");
            String selectedCard = "";
            boolean isValidCard = false;
    
            // Make sure player selects appropriate card
            while (!isValidCard) {
                System.out.print("Enter a card: ");
                selectedCard = scanner.nextLine();
                
                // Confirm input isnt empty
                if (selectedCard.trim().isEmpty()) {
                    System.out.println("Please enter a valid card.");
                    continue;
                }

                // Convert the input to lowercase
                selectedCard = selectedCard.toLowerCase();
    
                // Capitalize the first letter
                selectedCard = selectedCard.substring(0, 1).toUpperCase() + selectedCard.substring(1);
    
                // Check if the selected card matches one of the indices in the names array
                for (String name : names) {
                    if (selectedCard.equals(name)) {
                        isValidCard = true;
                        break;
                    }
                }
    
                if (!isValidCard) {
                    System.out.println("Invalid card. Please enter a valid card.");
                }
            }

            ArrayList<String> previousCards = new ArrayList<String>();
            previousCards.add(selectedCard);
            int matchedCardCount = 0;
            List<Card> matchedCards = new ArrayList<>();
        
            for (Card card : computerHand) {
                //if a card matches
                if (card.getName().equals(selectedCard)) {
                    matchedCardCount++;
                    System.out.println("Card: " + card.getName() + " of " + card.getSuit() + " Matches: " + selectedCard);
                    matchedCards.add(card);
                }
            }
        
            // Remove matched cards from computerHand and add them to playerHand
            for (Card card : matchedCards) {
                computerHand.remove(card);
                playerHand.add(card);
            }
            Random rand = new Random();
            int randomIndex = rand.nextInt(deck.size());
            Card randomCard = deck.get(randomIndex);
            if (matchedCardCount == 0) {
                System.out.println("Go Fish! Draw a Card.");
                System.out.println(" ");
                //add random card from deck
                //System.out.println("RandomCard = "+ randomCard.getName() + " of "+ randomCard.getSuit());
                playerHand.add(randomCard);
                //remove card from deck
                deck.remove(randomCard);
                // Perform necessary actions when there's no matching
    
            }

            if (deck.isEmpty()) {
                break;
            } 
            


            // Check for sets
            SetChecker setChecker = new SetChecker();
            setChecker.checkCards(playerHand,playerSets);


            System.out.println("---Computer's Turn!---");
            String computerPick = context.pickCard(previousCards, computerHand);
            System.out.println("Computer Selects: "+computerPick);

            int matchedCCards = 0;
            List<Card> matchedCards2 = new ArrayList<>();
            for (Card card : playerHand) {
                //if a card matches
                if (card.getName().equals(computerPick)) {
                    matchedCCards++;
                    System.out.println("Card: " + card.getName() + " of " + card.getSuit() + " Matches: " + computerPick);
                    matchedCards2.add(card);
                }
            }
        
            // Remove matched cards from computerHand and add them to playerHand
            for (Card card : matchedCards2) {
                playerHand.remove(card);
                computerHand.add(card);
            }
            Random rand2 = new Random();
            int randomIndex2 = rand2.nextInt(deck.size());
            Card randomCard2 = deck.get(randomIndex2);
            if (matchedCCards == 0) {
                System.out.println("Go Fish! Draw a Card.");
                System.out.println(" ");
                computerHand.add(randomCard2);
                // Remove card from deck
                deck.remove(randomCard2);
                // Perform necessary actions when there's no matching
    
            }
            System.out.println("Computer Hand:");
            for (Card card : computerHand) {
                System.out.println(card.getName() + " of " + card.getSuit());
            }
            System.out.println(" ");
            // Check for sets
            setChecker.checkCards(computerHand,computerSets);

        }
        System.out.println("Game Over!");
        System.out.println("Player Sets:" + playerSets);
        System.out.println("Computer Sets:" + computerSets);

        if (playerSets.get() > computerSets.get()) {
            System.out.println("Player Wins");
        }

        else if (playerSets.get() < computerSets.get()) {
            System.out.println("Computer Wins");
        }

        else if (playerSets == computerSets) {
            System.out.println("Draw");
        }
        // --------------------------------------------------
    }
}
