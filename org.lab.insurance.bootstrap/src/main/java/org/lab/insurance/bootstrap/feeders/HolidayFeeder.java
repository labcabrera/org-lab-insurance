package org.lab.insurance.bootstrap.feeders;

import net.sf.flatpack.DataSet;

import org.apache.commons.lang.StringUtils;
import org.lab.insurance.model.jpa.common.Holiday;
import org.lab.insurance.model.jpa.common.HolidayCalendar;

public class HolidayFeeder extends AbstractEntityFeeder<Holiday> {

	@Override
	protected String getResourceName() {
		return "./holidays.csv";
	}

	@Override
	protected Holiday buildEntity(DataSet dataSet) {
		Holiday entity = new Holiday();
		entity.setCalendar(loadCalendarByName(dataSet.getString("CALENDAR")));
		entity.setName(dataSet.getString("NAME"));
		entity.setHolidayDate(parseDate(dataSet.getString("DATE")));
		return entity;
	}

	private HolidayCalendar loadCalendarByName(String name) {
		HolidayCalendar result = null;
		if (StringUtils.isNotEmpty(name)) {
			result = entityManagerProvider.get().createNamedQuery("HolidayCalendar.selectByName", HolidayCalendar.class).setParameter("name", name).getSingleResult();
		}
		return result;
	}
}
