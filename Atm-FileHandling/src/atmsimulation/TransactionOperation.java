package atmsimulation;

abstract public class TransactionOperation 
{
    protected String pin;
    protected String cardNo;

    public TransactionOperation(String pin, String cardNo) {
        this.pin = pin;
        this.cardNo = cardNo;
    }

    // Common method name for all transaction types
    public abstract void perform();
}
