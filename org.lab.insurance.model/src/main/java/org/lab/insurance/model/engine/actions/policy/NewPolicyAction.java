package org.lab.insurance.model.engine.actions.policy;

import java.util.Date;

import org.lab.insurance.model.HasPolicy;
import org.lab.insurance.model.engine.ActionDefinition;
import org.lab.insurance.model.engine.ActionEntity;
import org.lab.insurance.model.jpa.Policy;

@ActionDefinition(endpoint = "direct:new_policy_action")
@SuppressWarnings("serial")
public class NewPolicyAction implements ActionEntity<Policy>, HasPolicy {

	private Policy policy;
	private Date actionDate;

	@Override
	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	@Override
	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
}
