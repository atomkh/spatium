package com.urban.spatium.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.urban.spatium.dto.Board;
import com.urban.spatium.dto.Criteria;

@Mapper
public interface BoardMapper {
	public List<Board> getBoardsList(Board params); 
	
	public int getBoardTotalCount(Board params);

	/* public List<Board> getBoardsList(); */


	
	public List<Board> getBoardCate();

	public Board getBoardsByCode(int boardIdx);

	
	public int modifyPost(Board params);
	public int addPost(Board params);
	
	
	public int removePost(int boardIdx);
	
	
}
