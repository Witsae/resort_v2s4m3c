package dev.mvc.attachfile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.contents.ContentsProcInter;
import dev.mvc.contents.ContentsVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@Controller
public class AttachfileCont {
  @Autowired
  @Qualifier("dev.mvc.attachfile.AttachfileProc")
  private AttachfileProcInter attachfileProc;
  
  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;
  
  public AttachfileCont(){
    System.out.println("--> AttachfileCont created.");
  }
  
//http://localhost:9090/resort/attachfile/create.do
 /**
  * 등록 폼
  * @return
  */
  @RequestMapping(value="/attachfile/create.do", method=RequestMethod.GET )
  public ModelAndView create(int contentsno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/attachfile/create"); // webapp/attachfile/create.jsp
   
    return mav;
  }
  
  @RequestMapping(value = "/attachfile/create.do", method = RequestMethod.POST)
  public ModelAndView create(HttpServletRequest request,
                                           AttachfileVO attachfileVO) {
    // System.out.println("--> categrpno: " + categrpno);
    
    ModelAndView mav = new ModelAndView();
    // -----------------------------------------------------
    // 파일 전송 코드 시작
    // -----------------------------------------------------
    int contentsno = attachfileVO.getContentsno(); // 부모글 번호
    String fname = ""; // 원본 파일명
    String fupname = ""; // 업로드된 파일명
    long fsize = 0;  // 파일 사이즈
    String thumb = ""; // Preview 이미지
    int upload_count = 0; // 정상처리된 레코드 갯수
    
    String upDir = Tool.getRealPath(request, "/attachfile/storage");
    
    // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
    List<MultipartFile> fnamesMF = attachfileVO.getFnamesMF();
    
    int count = fnamesMF.size(); // 전송 파일 갯수
    if (count > 0) {
      for (MultipartFile multipartFile:fnamesMF) { // 파일 추출
        fsize = multipartFile.getSize();  // 파일 크기
        if (fsize > 0) { // 파일 크기 체크
          fname = multipartFile.getOriginalFilename(); // 원본 파일명
          fupname = Upload.saveFileSpring(multipartFile, upDir); // 파일 저장, 업로드 된 파일명
          
          if (Tool.isImage(fname)) { // 이미지인지 검사
            thumb = Tool.preview(upDir, fupname, 200, 150); // thumb 이미지 생성
          }
        }
        AttachfileVO vo = new AttachfileVO();
        vo.setContentsno(contentsno);
        vo.setFname(fname);
        vo.setFupname(fupname);
        vo.setThumb(thumb);
        vo.setFsize(fsize);
        
        // 파일 1건 등록 정보 dbms 저장, 파일이 20개면 20개의 record insert
        upload_count = upload_count + attachfileProc.create(vo); 
      }
    }    
    // -----------------------------------------------------
    // 파일 전송 코드 종료
    // -----------------------------------------------------
    
    /*
     * ra.addAttribute("upload_count", upload_count); // 업로드 된 총 파일의 개수
     * ra.addAttribute("categrpno", categrpno); ra.addAttribute("contentsno",
     * attachfileVO.getContentsno());
     * 
     * mav.setViewName("redirect:/attachfile/create_msg.jsp");
     */
    ContentsVO contentsVO = contentsProc.read(contentsno);
    
    mav.addObject("upload_count", upload_count); // redirect parameter 적용
    // mav.addObject("categrpno", categrpno); // redirect parameter 적용
    mav.addObject("contentsno", attachfileVO.getContentsno()); // redirect parameter 적용
    mav.addObject("cateno", contentsVO.getCateno());
    // mav.addObject("cateno", attachfileVO.getCateno()); // redirect parameter 적용
    mav.addObject("url", "create_msg"); // create_msg.jsp, redirect parameter 적용
    
    mav.setViewName("redirect:/attachfile/msg.do");
    
    return mav;
  }
  
  /**
   * 새로고침을 방지하는 메시지 출력
   * @param attachfileno
   * @return
   */
  @RequestMapping(value="/attachfile/msg.do", method=RequestMethod.GET)
  public ModelAndView msg(String url){
    ModelAndView mav = new ModelAndView();
    
    // 등록 처리 메시지: create_msg --> /member/create_msg.jsp
    // 수정 처리 메시지: update_msg --> /member/update_msg.jsp
    // 삭제 처리 메시지: delete_msg --> /member/delete_msg.jsp
    mav.setViewName("/attachfile/" + url); // forward
    
    return mav; // forward
  }
  
  /**
   * 목록
   * http://localhost:9090/ojt/attachfile/list.do
   * 
   * @return
   */
  @RequestMapping(value = "/attachfile/list.do", method = RequestMethod.GET)
  public ModelAndView list() {
    ModelAndView mav = new ModelAndView();

    List<AttachfileVO> list = attachfileProc.list();
    mav.addObject("list", list);

    mav.setViewName("/attachfile/list");

    return mav;
  }
  
  /**
   * 목록
   * http://localhost:9090/ojt/attachfile/list_by_contentsno.do
   * 
   * @return
   */
  @RequestMapping(value = "/attachfile/list_by_contentsno.do", method = RequestMethod.GET)
  public ModelAndView list_by_contentsno(int contentsno) {
    ModelAndView mav = new ModelAndView();

    List<AttachfileVO> list = attachfileProc.list_by_contentsno(contentsno);
    mav.addObject("list", list);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("title", contentsVO.getTitle());

    mav.setViewName("/attachfile/list_by_contentsno");

    return mav;
  }
  
  /**
   * 첨부 파일 1건 삭제 처리
   * 
   * @return
   */
  @RequestMapping(value = "/attachfile/delete.do", 
                             method = RequestMethod.POST)
  public ModelAndView delete_proc(HttpServletRequest request,
                                                int attachfileno, String rurl) {
    ModelAndView mav = new ModelAndView();

    // 삭제할 파일 정보를 읽어옴.
    AttachfileVO attachfileVO = attachfileProc.read(attachfileno);
    
    String upDir = Tool.getRealPath(request, "/attachfile/storage"); // 절대 경로
    Tool.deleteFile(upDir, attachfileVO.getFupname()); // Folder에서 1건의 파일 삭제
    Tool.deleteFile(upDir, attachfileVO.getThumb()); // 1건의 Thumb 파일 삭제
    mav.addObject("contentsno", attachfileVO.getContentsno());
    
    // DBMS에서 1건의 파일 삭제 
    attachfileProc.delete(attachfileno);
        
    List<AttachfileVO> list = attachfileProc.list();
    mav.addObject("list", list);

    mav.setViewName("redirect:/attachfile/" + rurl); 

    return mav;
  }
  
  //http://localhost:9090/resort/attachfile/count_by_contentsno.do?contentsno=1
  /**
   * 부모 키 별 갯수 산출
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/attachfile/count_by_contentsno.do", method=RequestMethod.GET ,
                                 produces = "text/plain;charset=UTF-8")
  public String count_by_contentsno(int contentsno) {  
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    int cnt = this.attachfileProc.count_by_contentsno(contentsno);
  
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
  
    return json.toString(); 
  }
 
  //http://localhost:9090/resort/attachfile/delete_by_contentsno.do?contentsno=1
   /**
   * FK 부모키를 이용한 모든 레코드 삭제
   * @param 
   * @param 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/attachfile/delete_by_contentsno.do", method = RequestMethod.POST,
                                produces = "text/plain;charset=UTF-8")
  public String delete_by_contentsno(HttpServletRequest request,
                                                         @RequestParam(value="contentsno", defaultValue = "0") int contentsno) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    List<AttachfileVO> list = this.attachfileProc.list_by_contentsno(contentsno);
    int cnt = 0; // 삭제된 레코드 갯수
   
    String upDir = Tool.getRealPath(request, "/attachfile/storage"); // 절대 경로
   
    for(AttachfileVO attachfileVO: list) { // 파일 갯수 만큼 순환
      Tool.deleteFile(upDir, attachfileVO.getFupname()); // Folder에서 1건의 파일 삭제
      Tool.deleteFile(upDir, attachfileVO.getThumb()); // 1건의 Thumb 파일 삭제
     
      attachfileProc.delete(attachfileVO.getAttachfileno()); // DBMS에서 1건의 파일 삭제
      cnt = cnt + 1;
    }
   
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
  
    return json.toString(); 
  }
  
  
  
}