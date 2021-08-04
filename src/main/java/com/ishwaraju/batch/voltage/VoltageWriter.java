package com.ishwaraju.batch.voltage;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ishwaraju.entity.Voltage;
import com.ishwaraju.repository.IVoltageRepository;

@Component
public class VoltageWriter implements ItemWriter<Voltage> {

	@Autowired
	private IVoltageRepository voltageRepository;

	@Override
	public void write(List<? extends Voltage> items) throws Exception {
		System.out.println("Writing voltage repository");
		voltageRepository.saveAll(items);

	}

}
