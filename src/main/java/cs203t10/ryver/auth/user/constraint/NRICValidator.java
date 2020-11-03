package cs203t10.ryver.auth.user.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NRICValidator implements ConstraintValidator<NRICConstraint, String> {
    private final int[] NRIC_CONST = {2, 7, 6, 5, 4, 3, 2};
    private final char[] S_T_NRIC_CHECKSUM = {'J', 'Z', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A'};
    private final char[] F_G_NRIC_CHECKSUM = {'X', 'W', 'U', 'T', 'R', 'Q', 'P', 'N', 'M', 'L', 'K'};

 	@Override
 	public boolean isValid(String nric, ConstraintValidatorContext constraintValidatorContext) {
        if (nric == null) {
            return true;
        }
        char first = nric.charAt(0);
        char last = nric.charAt(8);
        long sum = 0;
        for (int i = 1; i < 8; i++) {
            sum += (Integer.parseInt(nric, i, i+1, 10) * NRIC_CONST[i-1]);
        }
        if (first == 'T' || first == 'G') {
            sum += 4;
        }
        int checksum = Math.toIntExact(sum % 11);
        if (first == 'S' || first == 'T') {
            return last == S_T_NRIC_CHECKSUM[checksum];
        } else {
            return last == F_G_NRIC_CHECKSUM[checksum];
        }
	}
}