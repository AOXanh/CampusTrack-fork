package com.jabai.campustrack.Validations.Validators;

import com.jabai.campustrack.Validations.Interfaces.PasswordConfirmable;
import com.jabai.campustrack.Validations.Interfaces.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordConfirmable> {
  @Override
  public boolean isValid(PasswordConfirmable passwordConfirmable, ConstraintValidatorContext constraintValidatorContext) {
    if (passwordConfirmable == null)
      return true;

    String password = passwordConfirmable.getPassword();
    String confirmation = passwordConfirmable.getPasswordConfirmation();

    if (password == null || confirmation == null)
      return true;
    if (password.equals(confirmation))
      return true;

    constraintValidatorContext.disableDefaultConstraintViolation();
    constraintValidatorContext
            .buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
            .addPropertyNode("passwordConfirmation")
            .addConstraintViolation();

    return false;
  }
}
