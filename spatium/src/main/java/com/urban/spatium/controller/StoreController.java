package com.urban.spatium.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.urban.spatium.dto.RefundPolicy;
import com.urban.spatium.dto.Store;
import com.urban.spatium.service.CalcService;
import com.urban.spatium.service.ItemService;
import com.urban.spatium.service.RefundService;
import com.urban.spatium.service.SpaceService;
import com.urban.spatium.service.StoreService;

@Controller
public class StoreController {

	@Autowired
	private StoreService storeService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private SpaceService spaceService;
	@Autowired
	private CalcService calcService;
	
	/* 업체 리스트 페이지에서 업체 삭제시 들어오는 컨트롤러 */
	@GetMapping("/store/admin/storeDelete")
	public String storeDelete(Model model
								,@RequestParam(name = "storeCode", required = false) String storeCode) {
		
		System.out.println(storeCode);
		
		refundService.refundRelationDelete(storeCode);
		refundService.refundPolicyDelete(storeCode);
		calcService.CalcDailyDelete(storeCode);
		itemService.itemDetailDelete(storeCode);
		spaceService.spaceRelationDelet(storeCode);
		spaceService.spaceReadyDelete(storeCode);
		spaceService.spaceOKDelete(storeCode);
		storeService.storeDelete(storeCode);
		
		return "redirect:/store/admin/storeListOK";
	}
	
	/* 메인 페이지에서 업체 등록 버튼 클릭시 들어오는 컨트롤러*/
	@GetMapping("/store/seller/myStoreInsert")
	public String myStoreInsert(Model model, HttpSession session, Store store) {
		
		model.addAttribute("title", "업체 등록");
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(sessionId);
		store.setStoreId(sessionId);
		String storeId = store.getStoreId();
		if(storeId.equals(null)) {
			System.out.println("로그인 후 이용해주시기 바랍니다.");
		}
		model.addAttribute("storeId", storeId);
		
		return "store/seller/myStoreInsert";
	}
	
	/* 메인 페이지에서 수정 버튼 클릭시 들어오는 포스트 맵핑 컨트롤러
	 * (수정처리) */
	@PostMapping("/store/seller/storeUpdate")
	public String myStoreUpdate(Model model, Store store) {
		
		String result = storeService.updateStoreSet(store);
		System.out.println(result);
		model.addAttribute("result", result);
		
		return "redirect:/store/myStore";
	}
	
	/* 메인 페이지에서 본인 업체 조회 후 수정할 때 들어오는 컨트롤러 */
	@GetMapping("/store/seller/myStoreUpdate")
	public String myStoreUpdate(Model model, Store store
								,@RequestParam(name = "storeCode", required = false) int storeCode) {
		
			System.out.println(storeCode);
			Store result = storeService.updateStore(storeCode);
			System.out.println(storeCode + "==== 업체 수정에서 가져온 스토어 코드 ====");
			
			List<RefundPolicy> refundPolicy =refundService.getRefundPolicy(storeCode);
			System.out.println(refundPolicy + "storeUpdate 컨트롤러 받은 값");
			model.addAttribute("title", "업체 수정");
			model.addAttribute("result", result);
			model.addAttribute("result2", refundPolicy);
			
		
		return "store/seller/myStoreUpdate";
	}
	
	/* 메인 페이지에서 본인 업체 조회 후 자세히 볼 때 들어오는 컨트롤러 */
	@GetMapping("/store/seller/myStoreSeeMore")
	public String myStoreSeeMore(Model model, Store store
			,@RequestParam(name = "storeCode", required = false) int storeCode) {
		
		System.out.println(storeCode);
		Store storeList = storeService.getStoreInfoByStoreCode(storeCode);
		List<RefundPolicy> storeList2 = refundService.getRefundPolicy(storeCode);
		System.out.println(storeList);
		model.addAttribute("title", "업체정보");
		model.addAttribute("storeList", storeList);
		model.addAttribute("storeList2", storeList2);		
		
		return "store/seller/myStoreSeeMore";
	}
	
	/* 메인 페이지에서 본인 업체 조회*/
	@GetMapping("/store/myStore")
	public String myStore(HttpSession session, Store store, Model model) {
			String storeId = (String) session.getAttribute("SID");
			System.out.println(storeId);
			List<Store> storeList = storeService.myStoreList(storeId);
		
			store.setStoreId(storeId);
			model.addAttribute("storeList", storeList);
			
		return "store/seller/myStore";
	}
	
	/* 업체명 클릭시 자세히 보기 위해서 들어오는 컨트롤러 */
	@GetMapping("/store/admin/storeSeeMore")
	public String storeSeeMore(Model model, Store store
								,@RequestParam(name = "storeCode", required = false) int storeCode) {
		
			System.out.println(storeCode);
			Store storeList = storeService.getStoreInfoByStoreCode(storeCode);
			List<RefundPolicy> storeList2 = refundService.getRefundPolicy(storeCode);
			System.out.println(storeList);
			model.addAttribute("title", "업체정보");
			model.addAttribute("storeList", storeList);
			model.addAttribute("storeList2", storeList2);
			
		return "store/admin/storeSeeMore";
	}
	
	/* 수정 폼에서 클릭시 들어오는 포스트 맵핑 컨트롤러
	 * (수정처리) */
	@PostMapping("/store/admin/storeUpdate")
	public String updateStoreSet(Model model, Store store) {
		
			String result = storeService.updateStoreSet(store);
			System.out.println(result);
			model.addAttribute("result", result);
			
		return "redirect:/store/admin/storeListOK";
	}
	
	/* 업체 목록에서 수정 버튼을 클릭시 들어오는 컨트롤러 */
	@GetMapping("/store/admin/storeUpdate")
	public String updateStore(Model model, Store store
							, @RequestParam(name = "storeCode", required = false) int storeCode) {
			Store result = storeService.updateStore(storeCode);
		System.out.println(storeCode + "==== 업체 수정에서 가져온 스토어 코드 ====");
		
		List<RefundPolicy> refundPolicy =refundService.getRefundPolicy(storeCode);
		System.out.println(refundPolicy + "storeUpdate 컨트롤러 받은 값");
			model.addAttribute("title", "업체 수정");
			model.addAttribute("result", result);
			model.addAttribute("result2", refundPolicy);
		return "store/admin/updateStore";
	}
	
	/* 공간 등록 폼에서 포스트 맵핑으로 들어오는 컨트롤러 */
	@PostMapping("/space/seller/addSpace")
	public String addMyStore(Model model, Store store , RefundPolicy refundPolicy){
		
		String fileName = null;
		
		if(!store.getProBgImage().isEmpty()) {
			fileName = store.getProBgImage().getOriginalFilename();
			String path = "C:\\Users\\ECS\\git\\spatium\\spatium\\src\\main\\resources\\static\\imges\\"; //패스 경로
			
			try {
				new File(path).mkdir(); 
				store.getProBgImage().transferTo(new File(path+fileName));
				
			} catch (IllegalStateException e) {   
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			store.setStoreImage(fileName);          
		}
		
		System.out.println(store);
		int checkStore = store.getStoreCode();
		System.out.println(checkStore);
		System.out.println("스토어 받은값 --> " + store);
		System.out.println("===============start store================");
		String storeCate = store.getStoreBusinessType();
		System.out.println("========== controller storeCode ============");
		List<String> tMap = new ArrayList<>();
		
		
		/*	길게 담겨진 문자열을 숫자 문자열로 변경하는 부분
		 *  (원래 서비스 단에서 해야하는 작업을 컨트롤러 단에서 작업 추후 변경 예정) */
		String[] array = storeCate.split(",");
		String[] i_array = new String[5];
		String arrayCheck = "0";
		for(int i=0; i<array.length; i++) {
			System.out.println(array[i]);
			
			if(array[i].equals("촬영 스튜디오")) {
				arrayCheck = "1";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("엠티장소")) {
				arrayCheck = "2";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("스터디룸")) {
				arrayCheck = "3";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("연습실")) {
				arrayCheck = "4";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("파티룸")) {
				arrayCheck = "5";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("회의실")) {
				arrayCheck = "6";	
				i_array[i]=arrayCheck;
			}else if(array[i].equals("카페")) {
				arrayCheck = "7";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("다목적홀")) {
				arrayCheck = "8";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("공연장")) {
				arrayCheck = "9";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("한옥")) {
				arrayCheck = "10";
				i_array[i]=arrayCheck;
			}
			System.out.println(arrayCheck + "arrayCheck 숫자");
			System.out.println(i_array[i] + "넣을 카테고리");
			tMap.add(i, i_array[i]);
		}
		
		model.addAttribute("storeBusiness", array);
		System.out.println("===============end store================");
		String result = storeService.addStore(store, tMap, refundPolicy);
		System.out.println("================start result===============");
		System.out.println(result);
		System.out.println("================end result===============");
		
		return "space/seller/addSpace";
	}
	
	/* 공간 등록 폼에서 포스트 맵핑으로 들어오는 컨트롤러 */
	@PostMapping("/space/admin/addSpace")
	public String addStore(Model model, Store store , RefundPolicy refundPolicy){
		
		   String fileName = null;
	          
	          if(!store.getProBgImage().isEmpty()) {
	             fileName = store.getProBgImage().getOriginalFilename();
	             String path = "C:\\Users\\ECS\\git\\spatium\\spatium\\src\\main\\resources\\static\\imges\\"; //패스 경로
	          
	             try {
	                new File(path).mkdir(); 
	               store.getProBgImage().transferTo(new File(path+fileName));
	               
	            } catch (IllegalStateException e) {   
	               e.printStackTrace();
	            } catch (IOException e) {
	               e.printStackTrace();
	            }
	             store.setStoreImage(fileName);          
	          }
		
		System.out.println(store);
		int checkStore = store.getStoreCode();
		System.out.println(checkStore);
		System.out.println("스토어 받은값 --> " + store);
		System.out.println("===============start store================");
		String storeCate = store.getStoreBusinessType();
		System.out.println("========== controller storeCode ============");
		List<String> tMap = new ArrayList<>();
		
		
		/*	길게 담겨진 문자열을 숫자 문자열로 변경하는 부분
		 *  (원래 서비스 단에서 해야하는 작업을 컨트롤러 단에서 작업 추후 변경 예정) */
		String[] array = storeCate.split(",");
		String[] i_array = new String[5];
		String arrayCheck = "0";
		for(int i=0; i<array.length; i++) {
			System.out.println(array[i]);
			
			if(array[i].equals("촬영 스튜디오")) {
				arrayCheck = "1";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("엠티장소")) {
				arrayCheck = "2";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("스터디룸")) {
				arrayCheck = "3";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("연습실")) {
				arrayCheck = "4";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("파티룸")) {
				arrayCheck = "5";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("회의실")) {
				arrayCheck = "6";	
				i_array[i]=arrayCheck;
			}else if(array[i].equals("카페")) {
				arrayCheck = "7";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("다목적홀")) {
				arrayCheck = "8";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("공연장")) {
				arrayCheck = "9";
				i_array[i]=arrayCheck;
			}else if(array[i].equals("한옥")) {
				arrayCheck = "10";
				i_array[i]=arrayCheck;
			}
			System.out.println(arrayCheck + "arrayCheck 숫자");
			System.out.println(i_array[i] + "넣을 카테고리");
			tMap.add(i, i_array[i]);
		}
		
		model.addAttribute("storeBusiness", array);
		System.out.println("===============end store================");
		String result = storeService.addStore(store, tMap, refundPolicy);
		System.out.println("================start result===============");
		System.out.println(result);
		System.out.println("================end result===============");
		
		return "space/admin/addSpace";
	}
	
	/* 왼쪽 메뉴에서 업체 등록 클릭시 들어오는 컨트롤러 */
	@GetMapping("/store/seller/addStore")
	public String addMyStore(Model model, HttpSession session, Store store) {
		model.addAttribute("title", "업체 등록");
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(sessionId);
		store.setStoreId(sessionId);
		String storeId = store.getStoreId();
		if(storeId.equals(null)) {
			System.out.println("로그인 후 이용해주시기 바랍니다.");
		}
		model.addAttribute("storeId", storeId);
		return "store/seller/addStore";
	}
	
	/* 왼쪽 메뉴에서 업체 등록 클릭시 들어오는 컨트롤러 */
	@GetMapping("/store/admin/addStore")
	public String addStore(Model model, HttpSession session, Store store) {
		model.addAttribute("title", "업체 등록");
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(sessionId);
		store.setStoreId(sessionId);
		String storeId = store.getStoreId();
		if(storeId.equals(null)) {
			System.out.println("로그인 후 이용해주시기 바랍니다.");
		}
		model.addAttribute("storeId", storeId);
		return "store/admin/addStore";
	}
	
	/* 왼쪽 메뉴에서 업체 리스트 클릭시 들어오는 컨트롤러 */
	@GetMapping("/store/admin/storeListOK")
	public String storeList(Model model) {
		List<Store> storeList = storeService.storeList();
		model.addAttribute("title", "업체 리스트");
		model.addAttribute("storeList", storeList);
		return "store/admin/storeListOK";
	}
}	