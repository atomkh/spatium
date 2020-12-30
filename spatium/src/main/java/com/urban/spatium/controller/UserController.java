package com.urban.spatium.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.urban.spatium.dto.User;
import com.urban.spatium.service.UserService;

@Controller
public class UserController {
	
	@Autowired 
	private UserService userService;
	
		
	//회원탈퇴
	@PostMapping("/removeMyinfo")
	public String removeMyinfo(@RequestParam(name="userId", required = false) String userId  
							  ,@RequestParam(name="userPw", required = false) String userPw
							  ,@RequestParam(name="userLevel", required = false) String userLevel
							  ,RedirectAttributes redirectAttr) { 
		System.out.println("회원탈퇴화면에서 입력받은 값(id)--->"	+ userId);
		System.out.println("회원탈퇴화면에서 입력받은 값(pw)--->"	+ userPw);
		System.out.println("회원탈퇴화면에서 입력받은 값(level)--->"+ userLevel);
		
		
		String result = userService.removeMyinfo(userId, userPw, userLevel);
		
		System.out.println(result);
		redirectAttr.addAttribute("result", result);
		
		return "redirect:/";
	}
	
	@GetMapping("/removeMyinfo")
	public String removeMyinfo( Model model
								,@RequestParam(name="userId", required = false) String userId
								,@RequestParam(name="userLevel", required = false) String userLevel) {
		model.addAttribute("title", "회원 탈퇴");
		model.addAttribute("userId", userId);
		model.addAttribute("userLevel", userLevel);
		return "user/userDelete";
	}
		
	//회원정보수정
	@PostMapping("/myInfo")
	public String myInfo(User user) {
		System.out.println("회원 수정 폼에서 입력받은 값" + user);
				
		String result = userService.myInfo(user);
		System.out.println(result);
		
		return "redirect:/myInfo";
	}
	
	@GetMapping("/myInfo")
	public String myInfo(Model model) {
	
			return "user/myInfo";
		}
	
	//회원삭제
	@PostMapping("/removeUser")
	public String removeUser(@RequestParam(name="userId", required = false) String userId  
							  ,@RequestParam(name="userPw", required = false) String userPw
							  ,@RequestParam(name="userLevel", required = false) String userLevel
							  ,RedirectAttributes redirectAttr) { 
		System.out.println("회원탈퇴화면에서 입력받은 값(id)--->"	+ userId);
		System.out.println("회원탈퇴화면에서 입력받은 값(pw)--->"	+ userPw);
		System.out.println("회원탈퇴화면에서 입력받은 값(level)--->"+ userLevel);
		
		
		String result = userService.removeUser(userId, userPw, userLevel);
		
		System.out.println(result);
		redirectAttr.addAttribute("result", result);
		
		return "redirect:/userList";
	}
	
	@GetMapping("/removeUser")
	public String removeUser( Model model
								,@RequestParam(name="userId", required = false) String userId
								,@RequestParam(name="userLevel", required = false) String userLevel) {
		model.addAttribute("title", "회원 탈퇴");
		model.addAttribute("userId", userId);
		model.addAttribute("userLevel", userLevel);
		return "user/uDelete";
	}
	
		
	//회원수정
	@PostMapping("/modifyUser")
	public String modifyUser(User user) {
		System.out.println("회원 수정 폼에서 입력받은 값" + user);
				
		String result = userService.modifyUser(user);
		System.out.println(result);
		
		return "redirect:/userList";
	}
	
	@GetMapping("/modifyUser")
	public String modifyUser( Model model
							   ,@RequestParam(name="userId", required = false) String userId) {
		System.out.println("회원 수정 폼에 보여질 회원아이디" + userId);
		
		User user = userService.login(userId);		
		
		System.out.println("db에서 검색한 회원정보-->" + user);
		
		model.addAttribute("title", "회원 수정화면");
		model.addAttribute("user", user);
		
		return "user/uUpdate";
	}	
	
	//탈퇴회원 리스트
	@GetMapping("/deleteUser")
	public String deleteUser(Model model) {
		return "user/deleteUser";
	}
	
	//불량회원 리스트
	@GetMapping("/blackUser")
	public String blackUser(Model model) {
		return "user/blackUser";
	}
	
	//휴면회원 리스트
	@GetMapping("/restUser")
	public String restUser(Model model) {
		return "user/restUser";
	}
	
	//회원리스트
	@PostMapping("/userList")
	public String userList(@RequestParam(name = "sk", required = false)String searchKey
			,@RequestParam(name = "sv", required = false)String searchValue
			,Model model) {
		/*log.info("화면에서 전달받은 파라미터값 sk :::::::  {}",searchKey);
		log.info("화면에서 전달받은 파라미터값 sv :::::::  {}",searchValue);*/
		if("아이디".equals(searchKey)) {
			searchKey="u_id";
		}else if("권한".equals(searchKey)) {
			searchKey="u_level";
			}if("관리자".equals(searchValue)) {
				searchValue="1";
			}else if("판매자".equals(searchValue)) {
				searchValue="2";
			}else if("구매자".equals(searchValue)) {
				searchValue="3";
			}else if("휴면회원".equals(searchValue)) {
				searchValue="4";
			}else if("탈퇴회원".equals(searchValue)) {
				searchValue="5";
			}else if("불량회원".equals(searchValue)) {
				searchValue="6";
			}			

	/*
	 * log.info("변경한 파라미터값 sv :::::::  {}",searchKey);
	 * log.info("변경한 파라미터값 sk :::::::  {}",searchValue);
	 */

		model.addAttribute("title", "회원 목록");
		/*List<User> userList = userService.getSearchUserList(searchKey,searchValue);*/
		
		return "user/userList";
	}
	
	@GetMapping("/userList")
	public String getUserList(Model model, @RequestParam(name="result", required = false) String result) {
		List<User> UserList = userService.getUserList();
		System.out.println(UserList);
		model.addAttribute("title", "회원 목록");
		model.addAttribute("userList", UserList);
		if(result != null) model.addAttribute("result", result);

		return "user/userList";
	}
	
	//비밀번호찾기
	
	  @GetMapping("/findPw") 
	  public String findPw(Model model) {
		  model.addAttribute("title", "비밀번호찾기");
		  return "user/findPw"; 
	  } 
	
	
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/login";
	}
	
	//로그인
	@PostMapping("/login")
	public String login( @RequestParam(name="userId", required = false) String userId
			,@RequestParam(name="userPw", required = false) String userPw
			,HttpSession session
			,RedirectAttributes rAttr) {

		System.out.println("로그인 화면에서 입력받은 값->" + userId);
		System.out.println("로그인 화면에서 입력받은 값->" + userPw);

		User User = userService.login(userId);

		if(userId != null && userPw != null && User != null && User.getUserPw() != null && userPw.equals(User.getUserPw())) {
			session.setAttribute("SID", userId);
			session.setAttribute("SLEVEL", User.getUserLevel());
			session.setAttribute("SNAME", User.getUserName());
			System.out.println(userId + " : 로그인 성공");
		}else {
			System.out.println(userId + " : 로그인 실패");
			return "redirect:/login";
		}

		return "user/userList";
	}
	
	@GetMapping("/login")
	public String login(Model model
			,@RequestParam(name="result", required = false) String result) {
		System.out.println("로그인 컨트롤러");
		model.addAttribute("title", "로그인 화면");
		if(result != null) model.addAttribute("result", result);

		return "user/login";
	}
	
	//아이디 중복체크
	@ResponseBody
	@RequestMapping(value="/idChk", method = RequestMethod.POST)
	public int idChk(User user) throws Exception {
		int result = userService.idChk(user);
		return result;
	}
	
	//회원가입
	@PostMapping("/addUser") 
	public String addUser(User user ,@RequestParam(name = "userId", required = false)
	  					String userId) {
	  System.out.println("회원가입화면에서 입력받은 값--->" + user); 
	  String result = userService.addUser(user); 
	  System.out.println(result);
	  	return "redirect:/userList"; 
	  }
	@GetMapping("/addUser") public String addUser(Model model) {
		  model.addAttribute("title", "회원 가입");
		  return "user/join";
		  }
	}  