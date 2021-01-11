package dev.mvc.categrp;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.admin.AdminProcInter;


@Controller
public class CategrpCont {
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc = null;
  
  @Autowired
  @Qualifier("dev.mvc.categrp.CategrpProc")
  private CategrpProcInter categrpProc;

  public CategrpCont() {
    System.out.println("--> CategrpCont created.");
  }

  /**
   * 등록폼 http://localhost:9090/resort/categrp/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/categrp/create.do", method = RequestMethod.GET)
  public ModelAndView create() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/categrp/create");

    return mav;
  }

  /**
   * 등록폼 http://localhost:9090/resort/categrp/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/categrp/create.do", method = RequestMethod.POST)
  public ModelAndView create(CategrpVO categrpVO) {
    // request.setAttribute("categrpVO", categrpVO) 자동 실행

    ModelAndView mav = new ModelAndView();
    // mav.setViewName("/categrp/create_msg"); // /webapp/categrp/create_msg.jsp

    int cnt = this.categrpProc.create(categrpVO);
    if (cnt == 0) { //에러 발생 시
      mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)
      mav.addObject("url", "create_msg");
      mav.setViewName("redirect:/categrp/msg.do");
    } else {
      mav.setViewName("redirect:/categrp/list.do");
    }
    return mav;
  }
  
  /**
   * AJAX등록폼 http://localhost:9090/resort/categrp/create_ajax.do
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/categrp/create_ajax.do", method = RequestMethod.POST,
                                produces = "text/plain;charset=UTF-8")
  public String create_ajax(CategrpVO categrpVO) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    int cnt = this.categrpProc.create(categrpVO);

    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
  
    return json.toString(); 
  }

  /**
   * 목록 http://localhost:9090/resort/categrp/list.do
   * 
   * @return
   */
  /*
   * @RequestMapping(value = "/categrp/list.do", method = RequestMethod.GET)
   * public ModelAndView list() { ModelAndView mav = new ModelAndView();
   * 
   * List<CategrpVO> list = this.categrpProc.list_seqno_asc();
   * mav.addObject("list", list);
   * 
   * //mav.setViewName("/categrp/list"); // /webapp/categrp/list.jsp
   * mav.setViewName("/categrp/list_ajax"); // /webapp/categrp/list.jsp
   * 
   * return mav; }
   */
  
  /**
   * 목록 http://localhost:9090/resort/categrp/list.do
   * 
   * @return
   */
//seqno 오름차순 출력 목록
 @RequestMapping(value="/categrp/list.do", method=RequestMethod.GET)
 public ModelAndView list(HttpSession session) {
   ModelAndView mav = new ModelAndView();
   
   if (adminProc.isAdmin(session)) { // 관리자 로그인인 경우
     List<CategrpVO> list = categrpProc.list_seqno_asc();
     
     mav.addObject("list", list);
     mav.setViewName("/categrp/list_ajax"); // /webapp/categrp/list.jsp      
   } else {
     mav.setViewName("/admin/login_need"); // /webapp/admin/login_need.jsp    
   }

   // mav.setViewName("redirect:/categrp/create_msg.jsp?count=" + count);
       
   return mav;
 }

  /**
   * 조회 + 수정폼 http://localhost:9090/resort/categrp/read_update.do
   * 
   * @return
   */
  @RequestMapping(value = "/categrp/read_update.do", method = RequestMethod.GET)
  public ModelAndView read_update(int categrpno) {
    // request.setAttribute("categrpVO", categrpVO) 자동 실행
    

    ModelAndView mav = new ModelAndView();
    mav.setViewName("/categrp/read_update"); // /webapp/categrp/read_update.jsp

    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);

    List<CategrpVO> list = this.categrpProc.list_categrpno_asc();
    mav.addObject("list", list);

    return mav;
  }
  
  /**
   * AJAX조회 + 수정폼 http://localhost:9090/resort/categrp/read_ajax.do
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/categrp/read_ajax.do", method = RequestMethod.GET,
                                produces = "text/plain;charset=UTF-8")
  public String read_ajax(int categrpno) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    
    JSONObject json = new JSONObject();
    json.put("categrpno", categrpno);
    json.put("name", categrpVO.getName());
    json.put("seqno", categrpVO.getSeqno());
    json.put("visible", categrpVO.getVisible());

    return json.toString();
  }
  
  /**
   * AJAX수정처리 http://localhost:9090/resort/categrp/update_ajax.do
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/categrp/update_ajax.do", method = RequestMethod.POST,
                                produces = "text/plain;charset=UTF-8")
  public String update_ajax(CategrpVO categrpVO) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    int cnt = this.categrpProc.update(categrpVO);

    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
  
    return json.toString(); 
  }
  
  /**
   * AJAX삭제처리 http://localhost:9090/resort/categrp/delete_ajax.do
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/categrp/delete_ajax.do", method = RequestMethod.POST,
                                produces = "text/plain;charset=UTF-8")
  public String delete_ajax(int categrpno) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    int cnt = this.categrpProc.delete(categrpno);

    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
  
    return json.toString(); 
  }

  //http://localhost:9090/resort/categrp/read_update.do
  /**
   * 수정 처리
   * 
   * @param categrpVO
   * @return
   */
  @RequestMapping(value = "/categrp/update.do", method = RequestMethod.POST)
  public ModelAndView update(CategrpVO categrpVO) {
    // CategrpVO categrpVO <FORM> 태그의 값으로 자동 생성됨.
    // request.setAttribute("categrpVO", categrpVO); 자동 실행
    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.update(categrpVO);
    mav.addObject("cnt", cnt); // request에 저장

    mav.setViewName("/categrp/update_msg"); // webapp/categrp/update_msg.jsp

    return mav;
  }

  /**
   * 조회 + 삭제폼 http://localhost:9090/resort/categrp/read_delete.do
   * 
   * @return
   */
  @RequestMapping(value = "/categrp/read_delete.do", method = RequestMethod.GET)
  public ModelAndView read_delete(int categrpno) {
    // request.setAttribute("categrpVO", categrpVO) 자동 실행

    ModelAndView mav = new ModelAndView();
    mav.setViewName("/categrp/read_delete"); // /webapp/categrp/read_update.jsp

    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);

    List<CategrpVO> list = this.categrpProc.list_categrpno_asc();
    mav.addObject("list", list);

    return mav;
  }

  /**
   * 삭제 처리
   * 
   * @param categrpno
   * @return
   */
  @RequestMapping(value = "/categrp/delete.do", method = RequestMethod.POST)
  public ModelAndView delete(int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.delete(categrpno);
    mav.addObject("cnt", cnt); // request에 저장

    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);
    
    if (cnt == 0) { //에러 발생 시
      mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)
      mav.addObject("url", "delete_msg");
      mav.setViewName("redirect:/categrp/msg.do");
    } else {
      mav.setViewName("redirect:/categrp/list.do");
    }

    return mav;
  }

//http://localhost:9090/resort/categrp/update_seqno_up.do?categrpno=1
  // http://localhost:9090/resort/categrp/update_seqno_up.do?categrpno=1000
  /**
   * 우선순위 상향 up 10 ▷ 1
   * 
   * @param categrpno 조회할 카테고리 번호
   * @return
   */
  @RequestMapping(value = "/categrp/update_seqno_up.do", method = RequestMethod.GET)
  public ModelAndView update_seqno_up(int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.update_seqno_up(categrpno);
    mav.addObject("cnt", cnt); // request에 저장

    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);

    mav.setViewName("/categrp/update_seqno_up_msg"); // webapp/categrp/update_seqno_up_msg.jsp

    return mav;
  }

  // http://localhost:9090/resort/categrp/update_seqno_down.do?categrpno=1
  // http://localhost:9090/resort/categrp/update_seqno_down.do?categrpno=1000
  /**
   * 우선순위 하향 up 1 ▷ 10
   * 
   * @param categrpno 조회할 카테고리 번호
   * @return
   */
  @RequestMapping(value = "/categrp/update_seqno_down.do", method = RequestMethod.GET)
  public ModelAndView update_seqno_down(int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.update_seqno_down(categrpno);
    mav.addObject("cnt", cnt); // request에 저장

    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);

    mav.setViewName("/categrp/update_seqno_down_msg"); // webapp/categrp/update_seqno_down_msg.jsp

    return mav;
  }

  /**
   * visible
   * 
   * @param categrpno 조회할 카테고리 번호
   * @return
   */
  @RequestMapping(value = "/categrp/update_visible.do", method = RequestMethod.GET)
  public ModelAndView update_visible(CategrpVO categrpVO) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.update_visible(categrpVO);
    mav.addObject("cnt", cnt);

    mav.setViewName("redirect:/categrp/list.do"); // webapp/categrp/update_visible_msg.jsp

    return mav;
  }

  /**
   * 새로고침을 방지하는 메시지 출력
   * 
   * @param categrpno
   * @return
   */
  @RequestMapping(value = "/categrp/msg.do", method = RequestMethod.GET)
  public ModelAndView msg(String url) {
    ModelAndView mav = new ModelAndView();

    // 등록 처리 메시지: create_msg --> /contents/create_msg.jsp
    // 수정 처리 메시지: update_msg --> /contents/update_msg.jsp
    // 삭제 처리 메시지: delete_msg --> /contents/delete_msg.jsp
    mav.setViewName("/categrp/" + url); // forward

    return mav; // forward
  }

}