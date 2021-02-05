package com.urban.spatium.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.urban.spatium.dto.OKSpace;
import com.urban.spatium.dto.ReadySpace;
import com.urban.spatium.dto.RefundPolicy;
import com.urban.spatium.dto.Store;
import com.urban.spatium.service.SpaceService;
import com.urban.spatium.service.StoreService;

@Controller
public class SpaceController {
	
	@Autowired
	private SpaceService spaceService;
	@Autowired
	private StoreService storeService;
	
	/* 메인 페이지에서 공간 등록 버튼 클릭시 들어오는 컨트롤러 */
	@GetMapping("/space/seller/mySpaceInsert")
	public String mySpaceInsert(Model model, HttpSession session, Store store) {
		
		String sid = (String) session.getAttribute("SID");
		String storeBusiness = storeService.myStoreCate(sid);
		String storeCode = storeService.storeCodeGet(sid);
		System.out.println(storeBusiness + "1111111111111");
		System.out.println(storeCode + "2222222222222222");
		String[] array = storeBusiness.split(",");
		model.addAttribute("storeBusiness", array);
		model.addAttribute("storeCode", storeCode);
		
		return "space/seller/mySpaceInsert";
	}
	
	@PostMapping("/space/seller/mySpaceInsert")
	public String mySpaceInsert(Model model, Store store, RefundPolicy refundPolicy) {
		
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
		
		return "space/seller/mySpaceInsert";
	}
	
	/* 메인 페이지에서 공간 등록 후 들어오는 컨트롤러 */
	@PostMapping("/space/mySpace")
	public String mySpace(Model model, ReadySpace readySpace, String storeCode) {
		
		spaceService.addReadySpace(readySpace, storeCode);
		
		return "redirect:/user/myPage";
	}
	
	/* 메인 페이지에서 본인 공간 조회*/
	@GetMapping("/space/mySpace")
	public String myReadySpaceList(HttpSession session, Model model, ReadySpace readySpace, OKSpace okSpace){
		
		String readySpaceId = (String) session.getAttribute("SID");
		readySpace.setreadySpaceId(readySpaceId);
		List<ReadySpace> myReadySpaceList = spaceService.myReadySpaceList(readySpaceId);
		List<OKSpace> myOKaySpaceList = spaceService.myOKSpaceList(readySpaceId);
		
		model.addAttribute("readySpaceList", myReadySpaceList);
		model.addAttribute("spaceListO", myOKaySpaceList);
		
		return "space/seller/mySpace";
	}
	
	/* 공간 승인 대기 목록에서 승인 버튼을 클릭 후 승인까지 완벽히 되었을시 들어오는 컨트롤러*/
	@PostMapping("/space/admin/spaceListOK")
	public String spaceAccept (Model model, HttpSession session, ReadySpace readySpace
							,@RequestParam(name = "readySpace", required = false) String readySpaceCode
							,@RequestParam(name = "spaceChk", required = false) String spaceChk) {
			
				System.out.println(readySpaceCode);
				System.out.println(spaceChk);
				String idChk = (String) session.getAttribute("SID");
				readySpace.setreadySpaceId(idChk);
				String readySpaceId = readySpace.getreadySpaceId();
				System.out.println(readySpaceId);
				if(spaceChk.equals("승인")) {
					spaceService.OKSpaceInsert(readySpaceId, readySpaceCode);
					spaceService.readySpaceDelete(readySpaceCode);
				}else if(spaceChk.equals("미승인")) {
					spaceService.readySpaceDelete(readySpaceCode);
				}
		
		return "redirect:/space/admin/spaceListOK";
	}
	
	/* 공간 승인 대기 목록에서 승인 버튼을 클릭할시 들어오는 컨트롤러 */
	@GetMapping("/space/admin/readySpaceAccept")
	public String readySpaceAccept (Model model, ReadySpace readySpaceDto
									,@RequestParam(name = "readySpace", required = false)int readySpaceCode) {
		
		List<ReadySpace> rsl = spaceService.readySpaceAccept(readySpaceCode);
		
		System.out.println(readySpaceCode);
		System.out.println(rsl + "11111111 rsl 부분");
		model.addAttribute("title", "공간 승인 화면");
		model.addAttribute("rsl", rsl);
		
		return "space/admin/readySpaceAccept";
	}
	
	/* 공간 등록 폼에서 포스트 맵핑으로 들어오는 컨트롤러
	 * (공간 등록 이후 바로 공간 승인 대기 리스트로 가게 됩니다.) */
	@PostMapping("/space/seller/spaceListReady")
	public String addMySpace(Model model, ReadySpace readySpace, String readyStoreBusinessType
			,@RequestParam(name = "readyStoreCode", required = false) String readyStoreCode) {
		System.out.println(readySpace);
		System.out.println(readyStoreCode + " readyStoreCode");
		String ReadyStore = spaceService.addReadySpace(readySpace, readyStoreCode);
		System.out.println(ReadyStore);
		
		return "redirect:/admin";
	}
	
	/* 공간 등록 폼에서 포스트 맵핑으로 들어오는 컨트롤러
	 * (공간 등록 이후 바로 공간 승인 대기 리스트로 가게 됩니다.) */
	@PostMapping("/space/admin/spaceListReady")
	public String addSpace(Model model, ReadySpace readySpace, String readyStoreBusinessType
								,@RequestParam(name = "readyStoreCode", required = false) String readyStoreCode) {
			System.out.println(readySpace);
			System.out.println(readyStoreCode + " readyStoreCode");
			String ReadyStore = spaceService.addReadySpace(readySpace, readyStoreCode);
			System.out.println(ReadyStore);
			
		return "redirect:/space/admin/spaceListReady";
	}
	
	/* 공간 등록 클릭 후 등록 하고자 하는 공간을 클릭시 들어오는 컨트톨러 */
	@GetMapping("/space/seller/addSpace")
	public String addMySpace(Model model, HttpSession session, Store store, String storeCode) {
		System.out.println(storeCode + "spacecontroller에서 받은 스토어 코드 값");
		model.addAttribute("title", "공간 등록");
		String storeId = (String) session.getAttribute("SID");
		store.setStoreId(storeId);
		String okId = store.getStoreId();
		System.out.println(okId + "스페이스 컨트롤러에서 받은 okId");
		String spaceCateList = spaceService.addSpace(storeCode);
		System.out.println(spaceCateList);
		String[] array = spaceCateList.split(",");
		model.addAttribute("storeBusiness", array);
		model.addAttribute("storeCode", storeCode);
		
		return "space/seller/addSpace";
	}
	
	/* 공간 등록 클릭 후 등록 하고자 하는 공간을 클릭시 들어오는 컨트톨러 */
	@GetMapping("/space/admin/addSpace")
	public String addSpace(Model model, HttpSession session, Store store, String storeCode) {
		System.out.println(storeCode + "spacecontroller에서 받은 스토어 코드 값");
		model.addAttribute("title", "공간 등록");
		String storeId = (String) session.getAttribute("SID");
		store.setStoreId(storeId);
		String okId = store.getStoreId();
		System.out.println(okId + "스페이스 컨트롤러에서 받은 okId");
		String spaceCateList = spaceService.addSpace(storeCode);
		System.out.println(spaceCateList);
		String[] array = spaceCateList.split(",");
		model.addAttribute("storeBusiness", array);
		model.addAttribute("storeCode", storeCode);
		
		return "space/admin/addSpace";
	}
	
	/* 왼쪽 메뉴에서 공간 등록 클릭시 들어오는 컨트롤러 */
	@GetMapping("/space/seller/addSpaceChoice")
	public String addMySpaceChoice(Model model, HttpSession session, Store store) {
		model.addAttribute("title", "공간 등록");
		String storeId = (String) session.getAttribute("SID");
		store.setStoreId(storeId);
		List<Store> spaceChoice = spaceService.addSpaceChoice(storeId);
		model.addAttribute("spaceChoice", spaceChoice);
		
		return "space/seller/addSpaceChoice";
	}
	
	/* 왼쪽 메뉴에서 공간 등록 클릭시 들어오는 컨트롤러 */
	@GetMapping("/space/admin/addSpaceChoice")
	public String addSpaceChoice(Model model, HttpSession session, Store store) {
		model.addAttribute("title", "공간 등록");
		String storeId = (String) session.getAttribute("SID");
		store.setStoreId(storeId);
		List<Store> spaceChoice = spaceService.addSpaceChoice(storeId);
		model.addAttribute("spaceChoice", spaceChoice);
		
		return "space/admin/addSpaceChoice";
	}
	
	/* 왼쪽 메뉴에서 공간 승인 대기 클릭시 들어오는 컨트롤러 */
	@GetMapping("/space/admin/spaceListReady")
	public String readySpaceList(Model model) {
		List<ReadySpace> readySpaceList = spaceService.readySpaceList();
			model.addAttribute("title", "공간 승인 대기 리스트");
			model.addAttribute("readySpaceList", readySpaceList);
		return "space/admin/readySpaceList";
	}
	
	/* 왼쪽 메뉴에서 공간 승인 완료 클릭시 들어오는 컨트롤러 */
	@GetMapping("/space/admin/spaceListOK")
	public String OKSpaceList(Model model) {
		List<OKSpace> spaceListO = spaceService.OKSpaceList();
			model.addAttribute("title", "공간 승인 완료 리스트");
			model.addAttribute("spaceListO", spaceListO);
		return "space/admin/OKSpaceList";
	}
}