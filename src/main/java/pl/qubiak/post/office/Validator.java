package pl.qubiak.post.office;

public class Validator {

    private final static String VIP_PIN = "8888";
    private final static String SUDDEN_PIN = "0000";
    private final static String VIP = "VIP";
    private final static String SUDDEN = "sudden";
    // Pins entered into the constants only for recruitment purposes.
    // Normally, each user should have his assigned and encrypted PIN saved in a separate table in the database.

    public static boolean validate(String role, String pin){
        if (role.equals(VIP) && !pin.equals(VIP_PIN)){
            return false;
        } else if (role.equals(SUDDEN) && !pin.equals(SUDDEN_PIN)){
            return false;
        }
        return true;
    }
}

