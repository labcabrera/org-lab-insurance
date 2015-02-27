package org.lab.insurance.model.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.lab.insurance.model.jpa.Policy;
import org.lab.insurance.model.jpa.insurance.Order;
import org.lab.insurance.model.jpa.insurance.OrderType;
import org.lab.insurance.model.matchers.OrderTypeMatcher;

import ch.lambdaj.Lambda;

public class PolicyValidator implements ConstraintValidator<ValidPolicy, Policy> {

	@Override
	public void initialize(ValidPolicy validPolicy) {
	}

	@Override
	public boolean isValid(Policy policy, ConstraintValidatorContext ctx) {
		boolean hasErrors = false;
		if (policy.getAgreement() == null) {
			ctx.buildConstraintViolationWithTemplate("policy.validation.missingAgreement").addConstraintViolation();
			hasErrors = true;
		}
		if (policy.getRelations() == null || policy.getRelations().isEmpty()) {
			ctx.buildConstraintViolationWithTemplate("policy.validation.missingRelations").addConstraintViolation();
			hasErrors = true;
		}
		List<Order> initialPayments = Lambda.select(policy.getOrders(), new OrderTypeMatcher(OrderType.INITIAL_PAYMENT));
		if (initialPayments.isEmpty()) {
			ctx.buildConstraintViolationWithTemplate("policy.validation.missingInitialPayment").addConstraintViolation();
			hasErrors = true;
		}
		return !hasErrors;
	}

}