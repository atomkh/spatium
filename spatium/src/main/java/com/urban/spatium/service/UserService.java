package com.urban.spatium.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urban.spatium.dto.User;
import com.urban.spatium.mapper.UserMapper;

@Service
@Transactional
public class UserService {

	@Autowired 
	private UserMapper userMapper;

	/*
	 * @Autowired UserRepository userRepository;
	 * 
	 * private JavaMailSender mailSender; private static final String FROM_ADDRESS =
	 * "본인의 이메일 주소를 입력하세요!";
	 */
    
    
	//불량회원 삭제
	public String removeBlack(String blackUserId) { 
		String result =	"불량회원 삭제 실패";

	int removeCheck = userMapper.removeBlack(blackUserId);
	if(removeCheck > 0) 
		result = "불량회원 삭제 완료"; 
	return result; 
	}

	//불량회원 리스트
	public List<User> blackUser(){
		List<User> blackUser = userMapper.blackUser(); 
		return blackUser; 
	}

	//불량회원 등록
	public String addBlackUser(User user) {
		String insertCheck = "불량회원 등록 실패";
		if(user != null) {
			int result = userMapper.addBlackUser(user);
			if(result > 0) insertCheck = "불량회원 등록 성공";
		}

		return insertCheck;
	}
	
	//휴면회원 리스트
		public List<User> restUser(){
			List<User> restUser = userMapper.restUser(); 
			return restUser; 
		}
	
	//탈퇴회원 리스트
	public List<User> deleteUser(){
		List<User> deleteUser = userMapper.deleteUser(); 
		return deleteUser; 
	}

	//회원탈퇴
	public void removeMyinfo(String userId, String userPw, String reason) {
		//1. 아이디로 유저 정보를 가져오는 쿼리문을 돌려서 user에 넣어준다.
		User user = userMapper.getUserById(userId);

		if(user != null && user.getUserPw() != null && userPw.equals(user.getUserPw())) {
			//2. user에다가 추가로 탈퇴 사유와 탈퇴 타입을 넣어준다.
			user.setDeleteReason(reason);
			user.setDeleteCate("일반 탈퇴");

			//3. 유저 정보를 delelteUser테이블에 백업한다.
			userMapper.addDeleteUser(user);    //하나의 서비스에서 여러 매퍼를 호출할 수 있다.

			//4. 유저정보를 죄다 (탈퇴)로 바꾼다
			userMapper.modifyDeleteUser(userId);
		}
	}

	//관리자용 회원삭제
	public void removeUser(String userId, String reason) {

		User user = userMapper.getUserById(userId);
		if(user != null) {
			user.setDeleteReason(reason);
			user.setDeleteCate("강제 탈퇴");
			userMapper.addDeleteUser(user);
			userMapper.modifyDeleteUser(userId);
		}
	}	

	//관리자 마이페이지 수정
	public String myInfo(User user) {
		String result = "마이페이지 수정 실패";

		int myInfoCheck = userMapper.myInfo(user);

		if(myInfoCheck > 0) result = "마이페이지 수정 성공";

		return result;
	}

	//구매자 마이페이지 수정
	public String myPage(User user) {
		String result = "마이페이지 수정 실패";
		int myInfoCheck = userMapper.myPage(user);
		if(myInfoCheck > 0) result = "마이페이지 수정 성공";
		return result;
	}


	//관리자용 회원수정
	public String modifyUser(User user) {
		String result = "회원 수정 실패";

		int modifyCheck = userMapper.modifyUser(user);

		if(modifyCheck > 0) result = "회원 수정 성공";

		return result;
	}

	//회원리스트
	public List<User> getUserList(){
		List<User> userList = userMapper.getUserList(); 
		return userList; 
	}

	//로그인
	public User login(String userId) {

		User user = userMapper.getUserById(userId);

		return user;
	}

	//관리자 아이디 중복체크
	public int idCheck(User user) {
		int result = userMapper.idCheck(user);
		return result;
	}

	//아이디 중복체크
	public int idChk(User user) {
		int result = userMapper.idChk(user);
		return result;
	}

	//관리자 회원가입	
	public String adminJoin(User user) {
		String insertCheck = "회원가입 실패";
		if(user != null) {
			int result = userMapper.adminJoin(user);
			if(result > 0) insertCheck = "회원가입 성공";
		}

		return insertCheck;
	}

	//구매자 회원가입	
	public String userJoin(User user) {
		String insertCheck = "회원가입 실패";
		if(user != null) {
			int result = userMapper.userJoin(user);
			if(result > 0) insertCheck = "회원가입 성공";
		}

		return insertCheck;
	}

	//아이디찾기
	public String findIdajax(String userEmail) {
		String userId = userMapper.findIdajax(userEmail);
		System.out.println("userId는  : " + userId);
		String result = null;
		if (userId == null) {
			result = "해당 이메일에 해당하는 아이디가 없습니다.";
		} else {
			result = "고객님의 아이디는 : "+userId+" 입니다.";
		}
		return result;
	}
	

}