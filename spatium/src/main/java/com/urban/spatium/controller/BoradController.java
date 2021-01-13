package com.urban.spatium.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.urban.spatium.dto.Board;
import com.urban.spatium.dto.BoardReply;
import com.urban.spatium.mapper.BoardMapper;
import com.urban.spatium.service.BoardService;


@Controller
public class BoradController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardMapper boardMapper;


	@GetMapping("/boardArticleList")
	public String boardArticleList() {
		return "borad/boardArticleList";
	}

	@GetMapping("/faqList")
	public String faqList() {
		return "borad/faqList";
	}
	
	

	@GetMapping(value = "/boardList")
	public String boardList(@ModelAttribute("params") Board board, Model model) {
		List<Board> boardList = boardService.getBoardsList(board);

		model.addAttribute("title", "소모임 게시판");
		model.addAttribute("boardList",boardList);
		return "borad/boardList";
	}


	//소모임 게시글 작성(view)
	 @RequestMapping("/addPost") 
	 public String addPost(Board board 
			 			   ,@RequestParam(name = "boardAddId", required = false) String boardAddId) {

		 if(boardAddId == null) {
				 return "redirect:/login";
		 }else {
			 String result = boardService.addPost(board); 
			 System.out.println(result); 
			 return "redirect:/boardList"; 
		 }
	 }
	 
	 //소모임 게시글 작성(Action)
	 @GetMapping("/boardWrite")
	 public String boardWrite(Model model
			 ,@RequestParam(name = "userId", required = false) String userId
			 ,HttpServletResponse response) throws IOException {
		 model.addAttribute("title", "소모임 게시판");
		 if(userId.equals("")) {
			 response.setContentType("text/html; charset=UTF-8");
			 PrintWriter out = response.getWriter();
			 out.println("<script>alert('로그인 후 이용해주세요'); location.href='/login';</script>");
			 out.flush();
			 return "redirect:/login";
		 }
		 else {
			 List<Board> boardCate = boardService.getBoardCate();
			 model.addAttribute("boardCate", boardCate);
			 return "borad/boardWrite";
		 }
		 
	 }
	 
	 //소모임 게시글 상세조회
	 @RequestMapping(value = "/detailPost", method = RequestMethod.GET)
		public String detailPost(@ModelAttribute("params") Board params
								 ,@RequestParam(name="boardIdx", required = false) int boardIdx
								 ,@RequestParam(name="currentPageNo", required = false) String currentPageNo
								 ,Model model
								 ,BoardReply boardReply) {
		
		Board board = boardService.getBoardsByCode(boardIdx);

		List<BoardReply> replysList = boardService.getReplyByCode(boardReply.getBoardIdx());
		model.addAttribute("replysList", replysList);
		System.out.println("replysList==>>" + replysList);
		
		/*boardService.postHitCnt(boardIdx);//조회수 증가 */
		model.addAttribute("title", "게시글 상세보기");
		model.addAttribute("Board", board);
		model.addAttribute("currentPageNo", currentPageNo);
		model.addAttribute("RecordsPerPage", params.getRecordsPerPage());
		model.addAttribute("SearchKeyword", params.getSearchKeyword());
		model.addAttribute("SearchType", params.getSearchType());
		
		return "borad/detailPost";
		}

	 //소모임 게시글 수정(view)
	 @GetMapping(value ="/modifyPost")
	 public String modifyPost(@ModelAttribute("params") Board params
			 				 ,@RequestParam(name="boardIdx", required = false) int boardIdx
			 				 ,Model model) {
		 
		 Board board = boardService.getBoardsByCode(boardIdx);
		 List<Board> boardCate = boardService.getBoardCate();
		 System.out.println(boardCate);

		 model.addAttribute("boardCate", boardCate);
		 model.addAttribute("title", "게시글 수정");
		 model.addAttribute("Board", board);

		 return "borad/modifyPost";
	 }

	 //소모임 게시글 수정(Action)
		@PostMapping(value ="/modifyPost")
		public String modifyPost(@ModelAttribute("params") Board params
								  ,RedirectAttributes rttr) {
			
			String result = boardService.modifyPost(params);

			rttr.addAttribute("CurrentPageNo", params.getCurrentPageNo());
			rttr.addAttribute("RecordsPerPage", params.getRecordsPerPage());
			rttr.addAttribute("SearchKeyword", params.getSearchKeyword());
			rttr.addAttribute("SearchType", params.getSearchType());
			
			System.out.println(result);
				
			return "redirect:/detailPost?boardIdx="+params.getBoardIdx();
		}

	//소모임 게시판 삭제(Action)
		@RequestMapping(value = "/removePost", method = RequestMethod.GET)
		public String removePost(@ModelAttribute("params") Board params
				,@RequestParam(name="boardIdx", required = false) int boardIdx
				,RedirectAttributes rttr) {

			String result = boardService.removePost(boardIdx);
			rttr.addAttribute("CurrentPageNo", params.getCurrentPageNo());
			rttr.addAttribute("RecordsPerPage", params.getRecordsPerPage());
			rttr.addAttribute("SearchKeyword", params.getSearchKeyword());
			rttr.addAttribute("SearchType", params.getSearchType());

			return "redirect:/boardList";
		}
		
		/**************************************************************************
		*********							댓글							***********
		**************************************************************************/
		
		//댓글 작성
		@RequestMapping(value="/addReply",  method = RequestMethod.POST)
		public String addReply(BoardReply boardReply, RedirectAttributes rttr
							   ,@ModelAttribute("params") Board params)  {
			
			String result = boardService.addReply(boardReply);
			
			

			rttr.addAttribute("boardIdx", boardReply.getBoardIdx());
			System.out.println("boardReply ==>>>" +  params.getRecordsPerPage());
			rttr.addAttribute("CurrentPageNo", params.getCurrentPageNo());
			rttr.addAttribute("RecordsPerPage", params.getRecordsPerPage());
			rttr.addAttribute("SearchKeyword", params.getSearchKeyword());
			rttr.addAttribute("SearchType", params.getSearchType());
			return "redirect:/detailPost";
		}
		
		//댓글 수정(View)
		@RequestMapping(value="/modifyReplyView",  method = RequestMethod.GET)
		public String modifyReplyView(@RequestParam(name="replyIdx", required = false) int replyIdx
									,Model model
								    ,@ModelAttribute("params") Board params) {
			 
			List<BoardReply> modifyReplyView =  boardService.getReplyByCode(replyIdx);
			model.addAttribute("modifyReplyView", modifyReplyView);
			model.addAttribute("params", params);
			
			return "borad/modifyReplyView";
		}
}
