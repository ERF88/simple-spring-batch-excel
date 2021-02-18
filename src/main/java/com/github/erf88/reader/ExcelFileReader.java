package com.github.erf88.reader;

import java.io.IOException;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.github.erf88.dto.CustomerDTO;

public class ExcelFileReader extends MultiResourceItemReader<CustomerDTO> {

	public ExcelFileReader(String pathIn) {
		Resource[] resources = getResources(pathIn);

		if (resources != null) {
			this.setResources(resources);
			this.setDelegate(getReader());
		}

	}

	private Resource[] getResources(String pathIn) {

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
		Resource[] resources = null;

		try {
			resources = resolver.getResources(pathIn);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resources;
	}

	private PoiItemReader<CustomerDTO> getReader() {
		PoiItemReader<CustomerDTO> reader = new PoiItemReader<>();
		reader.setRowMapper(getRowMapper());
		reader.setLinesToSkip(1);
		return reader;
	}

    private RowMapper<CustomerDTO> getRowMapper() {
        BeanWrapperRowMapper<CustomerDTO> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setTargetType(CustomerDTO.class);
        return rowMapper;
    }
	
}
