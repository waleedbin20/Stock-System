using System;
using System.Collections.Generic;

namespace Server
{
    class Stock
    {
        private readonly Dictionary<int, Account> accounts = new Dictionary<int, Account>();

        public void CreateAccount(int customerId, int accountNumber, int initialBalance)
        {
            Account account = new Account(customerId, accountNumber);
            account.Balance = initialBalance;
            accounts.Add(accountNumber, account);
            customerId ++;
            accountNumber ++;
        }

public int[] GetListOfAccounts(int customerId)
        {
            List<int> result = new List<int>();
            foreach (Account account in accounts.Values)
                if (account.CustomerId == customerId)
                    result.Add(account.AccountNumber);

            return result.ToArray();
        }

        public int GetAccountBalance(int customerId, int accountNumber)
        {
            if (accounts[accountNumber].CustomerId != customerId)
                throw new Exception($"Account {accountNumber} belongs to a different customer; customer {customerId} is not authorised to query balance for this account.");

            return accounts[accountNumber].Balance;
        }

        public void Transfer(int customerId, int fromAccount, int toAccount, int amount)
        {
            lock (accounts)
            {
                if (accounts[fromAccount].CustomerId != customerId)
                    throw new Exception($"Account {fromAccount} belongs to a different customer; customer {customerId} is not authorised to transfer from this account.");
                if (accounts[fromAccount].Balance < amount)
                    throw new Exception(
                        $"The balance of account {fromAccount} is {accounts[fromAccount].Balance} which is insufficient to transfer {amount}.");
                if (amount <= 0)
                    throw new Exception("Transfer amount has to be a positive value.");
                accounts[fromAccount].Balance -= amount;
                accounts[toAccount].Balance += amount;
            }
        }

        public int CreateAccount(int customerId)
        {
            lock (accounts)
            {
                int maxAccountNumber = 0;
                foreach (var accountNumber in accounts.Keys)
                    if (accountNumber > maxAccountNumber)
                        maxAccountNumber = accountNumber;

                var newAccountNumber = maxAccountNumber + 1;
                CreateAccount(customerId, newAccountNumber, 0);

                return newAccountNumber;
            }
        }
    }
}