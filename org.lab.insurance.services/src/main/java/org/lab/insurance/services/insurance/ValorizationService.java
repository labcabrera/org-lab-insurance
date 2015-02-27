package org.lab.insurance.services.insurance;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.lab.insurance.model.exceptions.NoCotizationException;
import org.lab.insurance.model.jpa.insurance.AssetPrice;
import org.lab.insurance.model.jpa.insurance.MarketOrder;
import org.lab.insurance.model.jpa.insurance.MarketOrderSource;
import org.lab.insurance.model.jpa.insurance.MarketOrderType;
import org.lab.insurance.model.jpa.insurance.Order;
import org.lab.insurance.model.matchers.MarketOrderTypeMatcher;

import ch.lambdaj.Lambda;

/**
 * Servicio que se encarga de valorizar un movimiento calculando el importe o las unidades a partir de las cotizaciones.
 */
public class ValorizationService {

	@Inject
	private CotizationsService cotizationsService;

	public void valorizate(Order order) throws NoCotizationException {
		for (MarketOrder i : Lambda.select(order.getMarketOrders(), new MarketOrderTypeMatcher(MarketOrderType.SELL))) {
			valorizate(i);
		}
		// TODO calcular el importe total de las ventas y actualizar el order
		for (MarketOrder i : Lambda.select(order.getMarketOrders(), new MarketOrderTypeMatcher(MarketOrderType.BUY))) {
			valorizate(i);
		}
	}

	private void valorizate(MarketOrder marketOrder) throws NoCotizationException {
		AssetPrice price = cotizationsService.getPrice(marketOrder.getAsset(), marketOrder.getDates().getValueDate());
		marketOrder.setNav(price.getPriceInEuros());
		if (marketOrder.getSource() == MarketOrderSource.UNITS) {
			Validate.notNull(marketOrder.getUnits());
			BigDecimal units = marketOrder.getUnits();
			BigDecimal amount = price.getPriceInEuros().multiply(units);
			marketOrder.setNetAmount(amount);
		} else {
			Validate.notNull(marketOrder.getNetAmount());
			BigDecimal amount = marketOrder.getNetAmount();
			Integer decimals = marketOrder.getAsset().getDecimals() != null ? marketOrder.getAsset().getDecimals() : 5;
			BigDecimal units = amount.divide(price.getPriceInEuros(), decimals, RoundingMode.HALF_EVEN);
			marketOrder.setUnits(units);
		}
	}
}