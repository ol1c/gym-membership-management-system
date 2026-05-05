package technical.task.gmms.exceptions;

public class MembershipCapacityExceededException extends RuntimeException{
    public MembershipCapacityExceededException(String message) {
        super(message);
    }
}
