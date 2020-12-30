package com.urban.spatium.mapper;	





import java.util.List;
import java.util.Map;	

import org.apache.ibatis.annotations.Mapper;

import com.urban.spatium.dto.RefundPolicy;	



@Mapper	
public interface RefundMapper {	

	public List<RefundPolicy> getRefundPolicy(int storeCode);	
	public int addRefundPolicy(Map<String, Object> rePolicyMap);	


}