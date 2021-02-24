using System;
using System.IO;
using System.Net.Sockets;

namespace Client
{
    class Client : IDisposable
    {
        const int port = 8888;

        private readonly StreamReader reader;
        private readonly StreamWriter writer;

        public Client(int customerId)
        {
            // Connecting to the server and creating objects for communications
            TcpClient tcpClient = new TcpClient("localhost", port);
            NetworkStream stream = tcpClient.GetStream();
            reader = new StreamReader(stream);
            writer = new StreamWriter(stream);

            // Sending customer ID
            writer.WriteLine(customerId);
            writer.Flush();

            // Parsing the response
            string line = reader.ReadLine();
            if (line.Trim().ToLower() != "success")
                throw new Exception(line);
        }

        public int[] GetAccountNumbers()
        {
            // Sending command
            writer.WriteLine("ACCOUNTS");
            writer.Flush();

            // Reading the number of accounts
            string line = reader.ReadLine();
            int numberOfAccounts = int.Parse(line);

            // Reading the account numbers
            int[] accounts = new int[numberOfAccounts];
            for (int i = 0; i < numberOfAccounts; i++)
            {
                line = reader.ReadLine();
                accounts[i] = int.Parse(line);
            }

            return accounts;
        }

        public int GetBalance(int accountNumber)
        {
            // Writing the command
            writer.WriteLine("BALANCE " + accountNumber);
            writer.Flush();

            // Reading the account balance
            string line = reader.ReadLine();
            return int.Parse(line);
        }

        public void Transfer(int fromAccount, int toAccount, int amount)
        {
            // Writing the command
            writer.WriteLine($"TRANSFER {fromAccount} {toAccount} {amount}");
            writer.Flush();

            // Reading the response
            string line = reader.ReadLine();
            if (line.Trim().ToLower() != "success")
                throw new Exception(line);
        }

        public int CreateAccount()
        {
            // Writing the command
            writer.WriteLine("CREATE_ACCOUNT");
            writer.Flush();

            // Reading the new account number
            string line = reader.ReadLine();
            return int.Parse(line);
        }

        public void Dispose()
        {
            reader.Close();
            writer.Close();
        }
    }
}