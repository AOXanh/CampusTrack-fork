package com.jabai.campustrack.Validations.Validators;

import com.jabai.campustrack.DTOs.Requests.RegisterUserRequestDto;
import com.jabai.campustrack.Validations.Interfaces.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterUserRequestDto> {
  @Override
  public boolean isValid(RegisterUserRequestDto registerUserRequestDto, ConstraintValidatorContext constraintValidatorContext) {
    String password = registerUserRequestDto.getPassword();
    String passwordConfirmation = registerUserRequestDto.getPasswordConfirmation();
    return password != null && password.equals(passwordConfirmation);
  }
}
