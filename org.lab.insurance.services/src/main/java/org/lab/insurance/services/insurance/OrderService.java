package org.lab.insurance.services.insurance;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.lab.insurance.model.Constants;
import org.lab.insurance.model.jpa.Policy;
import org.lab.insurance.model.jpa.insurance.Order;

public class OrderService {

	@Inject
	private Provider<EntityManager> entityManagerProvider;

	public List<Order> selectByPolicyInStates(Policy policy, List<String> states) {
		EntityManager entityManager = entityManagerProvider.get();
		String qlString = "select e from Order e where e.policy = :policy and e.currentState.stateDefinition.id in :stateIds order by e.dates.valueDate";
		TypedQuery<Order> query = entityManager.createQuery(qlString, Order.class);
		query.setParameter("stateIds", states);
		query.setParameter("policy", policy);
		return query.getResultList();
	}

	public List<Order> selectByPolicyNotInStates(Policy policy, String states) {
		EntityManager entityManager = entityManagerProvider.get();
		String qlString = "select e from Order e where e.policy = :policy and e.currentState.stateDefinition.id not in :stateIds order by e.dates.valueDate";
		TypedQuery<Order> query = entityManager.createQuery(qlString, Order.class);
		query.setParameter("stateIds", Arrays.asList(Constants.OrderStates.INITIAL));
		query.setParameter("policy", policy);
		return query.getResultList();
	}
}