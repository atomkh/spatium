package com.urban.spatium.controller;


import java.util.List;


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


	//탈퇴회원 업데이트
	@GetMapping("/user/admin/modifyDeleteUser")
	public String modifyDeleteUser( Model model
			,@RequestParam(name="userId", required = false) String userId) {
		System.out.println("회원 수정 폼에 보여질 회원아이디" + userId);

		User user = userService.login(userId);		

		System.out.println("db에서 검색한 회원정보-->" + user);

		model.addAttribute("title", "회원 수정화면");
		model.addAttribute("user", user);

		return "user/admin/uUpdate";
	}	

	//탈퇴회원 리스트
	@GetMapping("/user/admin/deleteUser")
	public String deleteUser(Model model) {
		List<User> deleteUser = userService.deleteUser();
		System.out.println(deleteUser);

		model.addAttribute("deleteUser", deleteUser);
		return "user/admin/deleteUser";
	}

	//관리자용 회원삭제
	@PostMapping("/user/admin/removeUser") 
	public String removeUser(@RequestParam(name="reason", required = false) String reason  
			,@RequestParam(name="userId", required = false) String userId
			,RedirectAttributes redirectAttr) { 
		System.out.println("회원탈퇴화면에서 입력받은 값(id)--->" + userId);
		System.out.println("회원탈퇴화면에서 입력받은 값(reason)--->"+ reason);

		userService.removeUser(userId, reason);
		
		return "redirect:/user/admin/userList";
	}	

	@GetMapping("/user/admin/removeUser")
	public String removeUser(Model model, HttpSession session
			,@RequestParam(name="userId", required = false) String userId) {
		model.addAttribute("title", "강제 탈퇴");
		model.addAttribute("userId", userId);

		return "user/admin/adminDelete";
	}

	//회원탈퇴
	@PostMapping("/user/removeMyinfo")
	public String removeMyinfo(@RequestParam(name="reason", required = false) String reason  
			,@RequestParam(name="userPw", required = false) String userPw
			,@RequestParam(name="userId", required = false) String userId
			,RedirectAttributes redirectAttr) { 
		System.out.println("회원탈퇴화면에서 입력받은 값(reason)--->"	+ reason);
		System.out.println("회원탈퇴화면에서 입력받은 값(pw)--->"	+ userPw);
		System.out.println("회원탈퇴화면에서 입력받은 값(id)--->"	+ userId);
		//가져온 값을 서비스로 가져가
		userService.removeMyinfo(userId,userPw,reason);

		return "redirect:/logout";
	}

	@GetMapping("/user/removeMyinfo")
	public String removeMyinfo( Model model
			,HttpSession session) {
		model.addAttribute("title", "회원 탈퇴");
		String SID = (String) session.getAttribute("SID");
		model.addAttribute("SID", SID);

		return "user/userDelete";
	}

	//판매자 마이페이지 수정
	@PostMapping("/user/seller/myInfo")
	public String mySellerInfo(User user) {
		System.out.println("회원 수정 폼에서 입력받은 값" + user);

		String result = userService.myInfo(user);
		System.out.println(result);

		return "redirect:/user/seller/myInfo";
	}
	
	@GetMapping("/user/seller/myInfo") 
	public String mySellerInfo(Model model, HttpSession session) {

		String userIdchk = (String) session.getAttribute("SID"); //로그인한 아이디를 가져오겠다는 코드
		System.out.println(userIdchk);
		User user = userService.login(userIdchk);	

		System.out.println("db에서 검색한 회원정보-->" + user);

		model.addAttribute("title", "회원 수정화면");
		model.addAttribute("user", user);
		return "/user/seller/myInfo";
	}
	
	//관리자 마이페이지 수정
	@PostMapping("/user/admin/myInfo")
	public String myInfo(User user) {
		System.out.println("회원 수정 폼에서 입력받은 값" + user);

		String result = userService.myInfo(user);
		System.out.println(result);

		return "redirect:/user/admin/myInfo";
	}
	
	@GetMapping("/user/admin/myInfo") 
	public String myInfo(Model model, HttpSession session) {

		String userIdchk = (String) session.getAttribute("SID"); //로그인한 아이디를 가져오겠다는 코드
		System.out.println(userIdchk);
		User user = userService.login(userIdchk);	

		System.out.println("db에서 검색한 회원정보-->" + user);

		model.addAttribute("title", "회원 수정화면");
		model.addAttribute("user", user);
		return "/user/admin/myInfo";
	}
	
	//구매자 마이페이지 수정
	@PostMapping("/user/userUpdate")
	public String userUpdate(User user) {
		System.out.println("회원 수정 폼에서 입력받은 값" + user);
		
		String result = userService.myPage(user);
		System.out.println(result);
		
		return "redirect:/user/userUpdate";
	}

	@GetMapping("/user/userUpdate") 
	public String userUpdate(Model model, HttpSession session) {

		String userIdchk = (String) session.getAttribute("SID"); //로그인한 아이디를 가져오겠다는 코드
		System.out.println(userIdchk);
		User user = userService.login(userIdchk);	

		System.out.println("db에서 검색한 회원정보-->" + user);

		model.addAttribute("title", "회원 수정화면");
		model.addAttribute("user", user);
		return "user/userUpdate";
	}
	
	@GetMapping("/user/myPage") 
	public String myPage(Model model, HttpSession session) {
		
		String userIdchk = (String) session.getAttribute("SID"); //로그인한 아이디를 가져오겠다는 코드
		System.out.println(userIdchk);
		User user = userService.login(userIdchk);	
		
		System.out.println("db에서 검색한 회원정보-->" + user);
		
		model.addAttribute("title", "회원 수정화면");
		model.addAttribute("user", user);
		return "user/myPage";
	}

	//관리자용 회원수정
	@PostMapping("/user/admin/modifyUser")
	public String modifyUser(User user) {
		System.out.println("회원 수정 폼에서 입력받은 값" + user);

		String result = userService.modifyUser(user);
		System.out.println(result);

		return "redirect:/user/admin/userList";
	}

	@GetMapping("/user/admin/modifyUser")
	public String modifyUser( Model model
			,@RequestParam(name="userId", required = false) String userId) {
		System.out.println("회원 수정 폼에 보여질 회원아이디" + userId);

		User user = userService.login(userId);		

		System.out.println("db에서 검색한 회원정보-->" + user);

		model.addAttribute("title", "회원 수정화면");
		model.addAttribute("user", user);

		return "user/admin/adminUpdate";
	}	

	//불량회원 삭제
	@GetMapping("/user/admin/removeBlack") 
	 public String removeBlack(Model model, @RequestParam(name="blackUserId", required = false) String blackUserId) {
		 System.out.println("장비파기 삭제화면에 입력받은 값 ->" + blackUserId);
		 userService.removeBlack(blackUserId);
		 model.addAttribute("blackUserId", blackUserId);
		 return "redirect:/user/admin/blackUser";
	  }
	
	//불량회원 리스트
	@GetMapping("/user/admin/blackUser")
	public String blackUser(Model model) {
		List<User> blackUser = userService.blackUser();
		System.out.println(blackUser);

		model.addAttribute("blackUser", blackUser);
		return "/user/admin/blackUser";
	}

	//불량회원 등록
	@PostMapping("/user/admin/addBlackUser") 
	public String addBlackUser(User user ,@RequestParam(name = "userId", required = false)	String userId) {
		System.out.println("불량회원 등록화면에서 입력받은 값--->" + user); 
		String result = userService.addBlackUser(user); 
		System.out.println(result);
		return "redirect:/user/admin/blackUser"; 
	}

	@GetMapping("/user/admin/addBlackUser") 
	public String addBlackUser(Model model) {
		model.addAttribute("title", "불량회원 등록");
		return "/user/admin/blackUserForm";
	}
	
	//휴면회원 리스트
	@GetMapping("/user/admin/restUser")
	public String restUser(Model model) {
		List<User> restUser = userService.restUser();
		
		model.addAttribute("restUser", restUser);
		return "/user/admin/restUser";
	}

	//회원리스트
	@PostMapping("/user/admin/userList")
	public String userList(Model model) {

		model.addAttribute("title", "회원 목록");

		return "/user/admin/userList";
	}

	@GetMapping("/user/admin/userList")
	public String getUserList(Model model, @RequestParam(name="result", required = false) String result) {
		List<User> UserList = userService.getUserList();
		System.out.println(UserList);
		model.addAttribute("title", "회원 목록");
		model.addAttribute("userList", UserList);
		if(result != null) model.addAttribute("result", result);

		return "/user/admin/userList";
	}

	//간편로그인 관리자
	@GetMapping("/imsilogin")
	public String imsilogin(HttpSession session) {
		session.setAttribute("SID", "id001");
		session.setAttribute("SLEVEL", "1");
		session.setAttribute("SNAME", "이순신");
		return "redirect:/";
	}
	//간편로그인 판매자
	@GetMapping("/sellerlogin")
	public String sellerlogin(HttpSession session) {
		session.setAttribute("SID", "id002");
		session.setAttribute("SLEVEL", "2");
		session.setAttribute("SNAME", "홍길동");
		return "redirect:/";
	}
	//간편로그인 구매자
	@GetMapping("/buyerlogin")
	public String buyerlogin(HttpSession session) {
		session.setAttribute("SID", "id015");
		session.setAttribute("SLEVEL", "3");
		session.setAttribute("SNAME", "주전자");
		return "redirect:/";
	}

	//로그아웃
	@GetMapping("/user/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/user/login";
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
			session.setAttribute("SLEVEL", Integer.toString(User.getUserLevel()));
			session.setAttribute("SNAME", User.getUserName());
			System.out.println(userId + " : 로그인 성공");
		}else {
			System.out.println(userId + " : 로그인 실패");
			return "redirect:/login";
		}
		return "redirect:/index";
	}

	@GetMapping("/login")
	public String login(Model model
			,@RequestParam(name="result", required = false) String result) {
		System.out.println("로그인 컨트롤러");
		model.addAttribute("title", "로그인 화면");
		if(result != null) model.addAttribute("result", result);

		return "user/login";
	}

	//아이디 찾기
	@GetMapping("/findIdform")
	public String findIdform()	{ 
		return "/user/findIdform"; 
	}

	//관리자아이디 중복체크
	@ResponseBody
	@RequestMapping(value="/user/admin/idCheck", method = RequestMethod.POST)
	public int idCheck(User user) {
		int result = userService.idCheck(user);
		return result;
	}
	
	//아이디 중복체크
	@ResponseBody
	@RequestMapping(value="/user/idChk", method = RequestMethod.POST)
	public int idChk(User user)  {
		int result = userService.idChk(user);
		return result;
	}

	//관리자 회원가입
	@PostMapping("/user/admin/adminJoin") 
	public String adminJoin(User user ,@RequestParam(name = "userId", required = false)
	String userId) {
		System.out.println("회원가입화면에서 입력받은 값--->" + user); 
		String result = userService.adminJoin(user); 
		System.out.println(result);
		return "redirect:/user/admin/userList"; 
	}
	
	@GetMapping("/user/admin/adminJoin") public String adminJoin(Model model) {
		model.addAttribute("title", "회원 가입");
		return "user/admin/adminJoin";
	}
	
	//구매자 회원가입
	@PostMapping("/user/userJoin") 
	public String userJoin(User user ,@RequestParam(name = "userId", required = false)
	String userId) {
		System.out.println("회원가입화면에서 입력받은 값--->" + user); 
		String result = userService.userJoin(user); 
		System.out.println(result);
		return "redirect:/main"; 
	}

	@GetMapping("/user/userJoin") 
	public String userJoin(Model model) {
		model.addAttribute("title", "회원 가입");
		return "/user/userJoin";
	}
	
	//아이디 찾기
	@ResponseBody
	@RequestMapping(value = "/findIdajax",produces = "application/json", method = RequestMethod.POST  )
	public String findIdajax(@RequestParam(name="userEmail", required = false) String userEmail) {
		System.out.println(userEmail);
		String result = userService.findIdajax(userEmail);
		return result;
	}
	
	@GetMapping("/user/findIdform")
	public String findIdform(Model model) throws Exception{
		return "/user/findIdform";
	}
	
}


