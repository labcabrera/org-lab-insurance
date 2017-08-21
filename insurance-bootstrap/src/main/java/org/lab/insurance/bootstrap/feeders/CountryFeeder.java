package org.lab.insurance.bootstrap.feeders;

import org.lab.insurance.model.geo.Country;

import net.sf.flatpack.DataSet;

public class CountryFeeder extends AbstractEntityFeeder<Country> {

	@Override
	protected String getResourceName() {
		return "./countries.csv";
	}

	@Override
	protected Country buildEntity(DataSet dataSet) {
		Country country = new Country();
		country.setId(dataSet.getString("ISO2"));
		country.setName(dataSet.getString("NAME"));
		country.setIso3(dataSet.getString("ISO3"));
		return country;
	}

}