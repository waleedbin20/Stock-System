namespace Server
{
    class Account
    {
        public Account(int customerId, int accountNumber)
        {
            AccountNumber = accountNumber;
            CustomerId = customerId;
        }

        public int AccountNumber { get; }
        public int CustomerId { get; }
        public int Balance { get; set; }
    }
}