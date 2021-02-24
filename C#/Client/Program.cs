using System;

namespace Client
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Enter your customer ID:");

            try
            {
                int customerId = int.Parse(Console.ReadLine());

                using (Client client = new Client(customerId))
                {
                    Console.WriteLine("Logged in successfully.");

                    while (true)
                    {
                        int[] accountNumbers = client.GetAccountNumbers();
                        Console.WriteLine("Your accounts:");
                        foreach (int account in accountNumbers)
                            Console.WriteLine($"  Account Number: {account} Number of stocks: {client.GetBalance(account)} ");

                        Console.WriteLine("Enter 'create' to create an account or 'transfer' to transfer or 'logout' to logout: ");
                        var choice = Console.ReadLine().Trim().ToLower();
                        switch (choice)
                        {
                            case "create":
                                int newAccount = client.CreateAccount();
                                Console.WriteLine($"Account {newAccount} created.");
                                break;

                            case "transfer":
                                Console.WriteLine("Enter the account number to transfer from or -1 to print the account list:");
                                int fromAccount = int.Parse(Console.ReadLine());
                                if (fromAccount < 0)
                                    continue;

                                Console.WriteLine(
                                    "Enter the account number to transfer to (this could be someone else's account):");
                                int toAccount = int.Parse(Console.ReadLine());

                                int amount = 1;

                                client.Transfer(fromAccount, toAccount, amount);
                                break;

                            case "logout":
                                client.Transfer(customerId, customerId + 1, 1);
                                client.Dispose();
                                break;

                            default:
                                Console.WriteLine($"Unknown command: {choice}");
                                break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}
