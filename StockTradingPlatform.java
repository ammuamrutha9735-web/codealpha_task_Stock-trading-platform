import java.util.*;

public class StockTradingPlatform {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Create stock market
        StockMarket market = new StockMarket();

        market.addStock(new Stock("AAPL", "Apple Inc.", 220.50));
        market.addStock(new Stock("GOOGL", "Alphabet Inc.", 175.25));
        market.addStock(new Stock("MSFT", "Microsoft Corporation", 420.75));
        market.addStock(new Stock("TSLA", "Tesla Inc.", 250.40));
        market.addStock(new Stock("AMZN", "Amazon", 185.60));

        // Create user
        System.out.println("==========================================");
        System.out.println("     WELCOME TO STOCK TRADING PLATFORM");
        System.out.println("==========================================");

        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();

        System.out.print("Enter your initial balance: ₹");

        double initialBalance;

        while (true) {
            try {
                initialBalance = scanner.nextDouble();

                if (initialBalance < 0) {
                    System.out.println("Balance cannot be negative. Try again.");
                    System.out.print("Enter your initial balance: ₹");
                } else {
                    break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.next();
                System.out.print("Enter your initial balance: ₹");
            }
        }

        scanner.nextLine();

        User user = new User(userName, initialBalance);

        int choice;

        do {

            System.out.println("\n==========================================");
            System.out.println("             MAIN MENU");
            System.out.println("==========================================");
            System.out.println("1. Display Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Update Stock Prices");
            System.out.println("7. Exit");
            System.out.println("==========================================");

            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                System.out.print("Enter your choice: ");
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    market.displayMarketData();
                    break;

                case 2:
                    buyStock(scanner, user, market);
                    break;

                case 3:
                    sellStock(scanner, user, market);
                    break;

                case 4:
                    user.displayPortfolio(market);
                    break;

                case 5:
                    user.displayTransactionHistory();
                    break;

                case 6:
                    market.updateStockPrices();
                    System.out.println("\nStock prices updated successfully!");
                    break;

                case 7:
                    System.out.println("\nThank you for using Stock Trading Platform!");
                    System.out.println("Goodbye " + user.getName() + "!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 7);

        scanner.close();
    }


    // BUY STOCK METHOD

    public static void buyStock(Scanner scanner, User user, StockMarket market) {

        market.displayMarketData();

        System.out.print("\nEnter Stock Symbol to Buy: ");
        String symbol = scanner.nextLine().toUpperCase();

        Stock stock = market.getStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found!");
            return;
        }

        System.out.print("Enter quantity: ");

        int quantity;

        try {
            quantity = scanner.nextInt();
            scanner.nextLine();

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return;
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid quantity.");
            scanner.nextLine();
            return;
        }

        user.buyStock(stock, quantity);
    }


    // SELL STOCK METHOD

    public static void sellStock(Scanner scanner, User user, StockMarket market) {

        if (user.getPortfolio().isEmpty()) {
            System.out.println("\nYou do not own any stocks.");
            return;
        }

        System.out.println("\nYour Current Holdings:");

        for (Map.Entry<String, PortfolioItem> entry :
                user.getPortfolio().entrySet()) {

            System.out.println(
                    entry.getKey() +
                            " - Quantity: " +
                            entry.getValue().getQuantity()
            );
        }

        System.out.print("\nEnter Stock Symbol to Sell: ");
        String symbol = scanner.nextLine().toUpperCase();

        Stock stock = market.getStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found!");
            return;
        }

        System.out.print("Enter quantity: ");

        int quantity;

        try {

            quantity = scanner.nextInt();
            scanner.nextLine();

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return;
            }

        } catch (InputMismatchException e) {

            System.out.println("Invalid quantity.");
            scanner.nextLine();
            return;
        }

        user.sellStock(stock, quantity);
    }
}


// ============================================================
// STOCK CLASS
// ============================================================

class Stock {

    private String symbol;
    private String companyName;
    private double price;

    public Stock(String symbol, String companyName, double price) {

        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


// ============================================================
// STOCK MARKET CLASS
// ============================================================

class StockMarket {

    private Map<String, Stock> stocks;

    public StockMarket() {

        stocks = new HashMap<>();
    }

    public void addStock(Stock stock) {

        stocks.put(stock.getSymbol(), stock);
    }

    public Stock getStock(String symbol) {

        return stocks.get(symbol);
    }

    public void displayMarketData() {

        System.out.println("\n==========================================");
        System.out.println("              MARKET DATA");
        System.out.println("==========================================");

        System.out.printf(
                "%-10s %-25s %-15s%n",
                "SYMBOL",
                "COMPANY",
                "PRICE"
        );

        System.out.println("------------------------------------------");

        for (Stock stock : stocks.values()) {

            System.out.printf(
                    "%-10s %-25s ₹%-15.2f%n",
                    stock.getSymbol(),
                    stock.getCompanyName(),
                    stock.getPrice()
            );
        }
    }


    // Simulate changing stock prices

    public void updateStockPrices() {

        Random random = new Random();

        for (Stock stock : stocks.values()) {

            double percentageChange =
                    -5 + (10 * random.nextDouble());

            double change =
                    stock.getPrice() *
                            percentageChange / 100;

            double newPrice =
                    stock.getPrice() + change;

            if (newPrice < 1) {
                newPrice = 1;
            }

            stock.setPrice(newPrice);
        }
    }
}


// ============================================================
// USER CLASS
// ============================================================

class User {

    private String name;
    private double balance;

    private Map<String, PortfolioItem> portfolio;

    private List<Transaction> transactionHistory;


    public User(String name, double balance) {

        this.name = name;
        this.balance = balance;

        portfolio = new HashMap<>();

        transactionHistory = new ArrayList<>();
    }


    public String getName() {
        return name;
    }


    public double getBalance() {
        return balance;
    }


    public Map<String, PortfolioItem> getPortfolio() {
        return portfolio;
    }


    // BUY STOCK

    public void buyStock(Stock stock, int quantity) {

        double totalCost =
                stock.getPrice() * quantity;

        if (totalCost > balance) {

            System.out.println("\nTransaction Failed!");
            System.out.println("Insufficient balance.");

            System.out.printf(
                    "Required: ₹%.2f%n",
                    totalCost
            );

            System.out.printf(
                    "Available: ₹%.2f%n",
                    balance
            );

            return;
        }

        balance -= totalCost;

        if (portfolio.containsKey(stock.getSymbol())) {

            PortfolioItem item =
                    portfolio.get(stock.getSymbol());

            item.addStock(quantity, stock.getPrice());

        } else {

            portfolio.put(
                    stock.getSymbol(),

                    new PortfolioItem(
                            stock.getSymbol(),
                            stock.getCompanyName(),
                            quantity,
                            stock.getPrice()
                    )
            );
        }


        Transaction transaction =
                new Transaction(
                        "BUY",
                        stock.getSymbol(),
                        quantity,
                        stock.getPrice()
                );

        transactionHistory.add(transaction);


        System.out.println("\nStock Purchased Successfully!");

        System.out.println(
                quantity + " shares of " +
                        stock.getSymbol() +
                        " purchased."
        );

        System.out.printf(
                "Total Cost: ₹%.2f%n",
                totalCost
        );

        System.out.printf(
                "Remaining Balance: ₹%.2f%n",
                balance
        );
    }


    // SELL STOCK

    public void sellStock(Stock stock, int quantity) {

        if (!portfolio.containsKey(stock.getSymbol())) {

            System.out.println(
                    "\nYou do not own this stock."
            );

            return;
        }

        PortfolioItem item =
                portfolio.get(stock.getSymbol());

        if (item.getQuantity() < quantity) {

            System.out.println(
                    "\nTransaction Failed!"
            );

            System.out.println(
                    "You only own " +
                            item.getQuantity() +
                            " shares."
            );

            return;
        }

        double totalValue =
                stock.getPrice() * quantity;

        balance += totalValue;

        item.removeStock(quantity);

        if (item.getQuantity() == 0) {

            portfolio.remove(stock.getSymbol());
        }


        Transaction transaction =
                new Transaction(
                        "SELL",
                        stock.getSymbol(),
                        quantity,
                        stock.getPrice()
                );

        transactionHistory.add(transaction);


        System.out.println(
                "\nStock Sold Successfully!"
        );

        System.out.println(
                quantity + " shares of " +
                        stock.getSymbol() +
                        " sold."
        );

        System.out.printf(
                "Amount Received: ₹%.2f%n",
                totalValue
        );

        System.out.printf(
                "Updated Balance: ₹%.2f%n",
                balance
        );
    }


    // DISPLAY PORTFOLIO

    public void displayPortfolio(StockMarket market) {

        System.out.println("\n==========================================");
        System.out.println(
                "           PORTFOLIO PERFORMANCE"
        );
        System.out.println("==========================================");

        System.out.println(
                "User: " + name
        );

        System.out.printf(
                "Available Balance: ₹%.2f%n",
                balance
        );


        if (portfolio.isEmpty()) {

            System.out.println(
                    "\nNo stocks in portfolio."
            );

            return;
        }


        System.out.println(
                "\nStock Holdings:"
        );

        System.out.println(
                "------------------------------------------------------------"
        );

        System.out.printf(
                "%-8s %-10s %-15s %-15s %-15s%n",
                "SYMBOL",
                "QTY",
                "AVG BUY PRICE",
                "CURRENT VALUE",
                "PROFIT/LOSS"
        );

        System.out.println(
                "------------------------------------------------------------"
        );


        double totalInvestment = 0;

        double currentPortfolioValue = 0;


        for (PortfolioItem item :
                portfolio.values()) {

            Stock stock =
                    market.getStock(
                            item.getSymbol()
                    );

            double investment =
                    item.getAverageBuyPrice() *
                            item.getQuantity();

            double currentValue =
                    stock.getPrice() *
                            item.getQuantity();

            double profitLoss =
                    currentValue -
                            investment;


            totalInvestment += investment;

            currentPortfolioValue += currentValue;


            System.out.printf(
                    "%-8s %-10d ₹%-14.2f ₹%-14.2f ₹%-14.2f%n",

                    item.getSymbol(),

                    item.getQuantity(),

                    item.getAverageBuyPrice(),

                    currentValue,

                    profitLoss
            );
        }


        System.out.println(
                "------------------------------------------------------------"
        );

        System.out.printf(
                "Total Investment: ₹%.2f%n",
                totalInvestment
        );

        System.out.printf(
                "Current Portfolio Value: ₹%.2f%n",
                currentPortfolioValue
        );

        System.out.printf(
                "Total Assets: ₹%.2f%n",

                balance + currentPortfolioValue
        );

        double totalProfitLoss =
                currentPortfolioValue -
                        totalInvestment;

        System.out.printf(
                "Overall Stock Profit/Loss: ₹%.2f%n",
                totalProfitLoss
        );
    }


    // DISPLAY TRANSACTION HISTORY

    public void displayTransactionHistory() {

        System.out.println("\n==========================================");
        System.out.println(
                "         TRANSACTION HISTORY"
        );
        System.out.println("==========================================");


        if (transactionHistory.isEmpty()) {

            System.out.println(
                    "No transactions available."
            );

            return;
        }


        for (Transaction transaction :
                transactionHistory) {

            transaction.displayTransaction();

            System.out.println(
                    "------------------------------------------"
            );
        }
    }
}


// ============================================================
// PORTFOLIO ITEM CLASS
// ============================================================

class PortfolioItem {

    private String symbol;

    private String companyName;

    private int quantity;

    private double averageBuyPrice;


    public PortfolioItem(
            String symbol,
            String companyName,
            int quantity,
            double averageBuyPrice
    ) {

        this.symbol = symbol;

        this.companyName = companyName;

        this.quantity = quantity;

        this.averageBuyPrice = averageBuyPrice;
    }


    public String getSymbol() {
        return symbol;
    }


    public int getQuantity() {
        return quantity;
    }


    public double getAverageBuyPrice() {
        return averageBuyPrice;
    }


    public void addStock(
            int newQuantity,
            double newPrice
    ) {

        double oldInvestment =
                quantity * averageBuyPrice;

        double newInvestment =
                newQuantity * newPrice;

        quantity += newQuantity;

        averageBuyPrice =
                (oldInvestment + newInvestment)
                        / quantity;
    }


    public void removeStock(int quantity) {

        this.quantity -= quantity;
    }
}


// ============================================================
// TRANSACTION CLASS
// ============================================================

class Transaction {

    private String type;

    private String stockSymbol;

    private int quantity;

    private double price;

    private Date date;


    public Transaction(
            String type,
            String stockSymbol,
            int quantity,
            double price
    ) {

        this.type = type;

        this.stockSymbol = stockSymbol;

        this.quantity = quantity;

        this.price = price;

        this.date = new Date();
    }


    public void displayTransaction() {

        System.out.println(
                "Transaction Type: " + type
        );

        System.out.println(
                "Stock: " + stockSymbol
        );

        System.out.println(
                "Quantity: " + quantity
        );

        System.out.printf(
                "Price per Share: ₹%.2f%n",
                price
        );

        System.out.printf(
                "Total Amount: ₹%.2f%n",
                price * quantity
        );

        System.out.println(
                "Date: " + date
        );
    }
}
