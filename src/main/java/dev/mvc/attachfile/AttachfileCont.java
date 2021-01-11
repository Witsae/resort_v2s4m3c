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
  * ��� ��
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
    // ���� ���� �ڵ� ����
    // -----------------------------------------------------
    int contentsno = attachfileVO.getContentsno(); // �θ�� ��ȣ
    String fname = ""; // ���� ���ϸ�
    String fupname = ""; // ���ε�� ���ϸ�
    long fsize = 0;  // ���� ������
    String thumb = ""; // Preview �̹���
    int upload_count = 0; // ����ó���� ���ڵ� ����
    
    String upDir = Tool.getRealPath(request, "/attachfile/storage");
    
    // ���� ������ ����� fnamesMF ��ü�� ������.
    List<MultipartFile> fnamesMF = attachfileVO.getFnamesMF();
    
    int count = fnamesMF.size(); // ���� ���� ����
    if (count > 0) {
      for (MultipartFile multipartFile:fnamesMF) { // ���� ����
        fsize = multipartFile.getSize();  // ���� ũ��
        if (fsize > 0) { // ���� ũ�� üũ
          fname = multipartFile.getOriginalFilename(); // ���� ���ϸ�
          fupname = Upload.saveFileSpring(multipartFile, upDir); // ���� ����, ���ε� �� ���ϸ�
          
          if (Tool.isImage(fname)) { // �̹������� �˻�
            thumb = Tool.preview(upDir, fupname, 200, 150); // thumb �̹��� ����
          }
        }
        AttachfileVO vo = new AttachfileVO();
        vo.setContentsno(contentsno);
        vo.setFname(fname);
        vo.setFupname(fupname);
        vo.setThumb(thumb);
        vo.setFsize(fsize);
        
        // ���� 1�� ��� ���� dbms ����, ������ 20���� 20���� record insert
        upload_count = upload_count + attachfileProc.create(vo); 
      }
    }    
    // -----------------------------------------------------
    // ���� ���� �ڵ� ����
    // -----------------------------------------------------
    
    /*
     * ra.addAttribute("upload_count", upload_count); // ���ε� �� �� ������ ����
     * ra.addAttribute("categrpno", categrpno); ra.addAttribute("contentsno",
     * attachfileVO.getContentsno());
     * 
     * mav.setViewName("redirect:/attachfile/create_msg.jsp");
     */
    ContentsVO contentsVO = contentsProc.read(contentsno);
    
    mav.addObject("upload_count", upload_count); // redirect parameter ����
    // mav.addObject("categrpno", categrpno); // redirect parameter ����
    mav.addObject("contentsno", attachfileVO.getContentsno()); // redirect parameter ����
    mav.addObject("cateno", contentsVO.getCateno());
    // mav.addObject("cateno", attachfileVO.getCateno()); // redirect parameter ����
    mav.addObject("url", "create_msg"); // create_msg.jsp, redirect parameter ����
    
    mav.setViewName("redirect:/attachfile/msg.do");
    
    return mav;
  }
  
  /**
   * ���ΰ�ħ�� �����ϴ� �޽��� ���
   * @param attachfileno
   * @return
   */
  @RequestMapping(value="/attachfile/msg.do", method=RequestMethod.GET)
  public ModelAndView msg(String url){
    ModelAndView mav = new ModelAndView();
    
    // ��� ó�� �޽���: create_msg --> /member/create_msg.jsp
    // ���� ó�� �޽���: update_msg --> /member/update_msg.jsp
    // ���� ó�� �޽���: delete_msg --> /member/delete_msg.jsp
    mav.setViewName("/attachfile/" + url); // forward
    
    return mav; // forward
  }
  
  /**
   * ���
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
   * ���
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
   * ÷�� ���� 1�� ���� ó��
   * 
   * @return
   */
  @RequestMapping(value = "/attachfile/delete.do", 
                             method = RequestMethod.POST)
  public ModelAndView delete_proc(HttpServletRequest request,
                                                int attachfileno, String rurl) {
    ModelAndView mav = new ModelAndView();

    // ������ ���� ������ �о��.
    AttachfileVO attachfileVO = attachfileProc.read(attachfileno);
    
    String upDir = Tool.getRealPath(request, "/attachfile/storage"); // ���� ���
    Tool.deleteFile(upDir, attachfileVO.getFupname()); // Folder���� 1���� ���� ����
    Tool.deleteFile(upDir, attachfileVO.getThumb()); // 1���� Thumb ���� ����
    mav.addObject("contentsno", attachfileVO.getContentsno());
    
    // DBMS���� 1���� ���� ���� 
    attachfileProc.delete(attachfileno);
        
    List<AttachfileVO> list = attachfileProc.list();
    mav.addObject("list", list);

    mav.setViewName("redirect:/attachfile/" + rurl); 

    return mav;
  }
  
  //http://localhost:9090/resort/attachfile/count_by_contentsno.do?contentsno=1
  /**
   * �θ� Ű �� ���� ����
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
   * FK �θ�Ű�� �̿��� ��� ���ڵ� ����
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
    int cnt = 0; // ������ ���ڵ� ����
   
    String upDir = Tool.getRealPath(request, "/attachfile/storage"); // ���� ���
   
    for(AttachfileVO attachfileVO: list) { // ���� ���� ��ŭ ��ȯ
      Tool.deleteFile(upDir, attachfileVO.getFupname()); // Folder���� 1���� ���� ����
      Tool.deleteFile(upDir, attachfileVO.getThumb()); // 1���� Thumb ���� ����
     
      attachfileProc.delete(attachfileVO.getAttachfileno()); // DBMS���� 1���� ���� ����
      cnt = cnt + 1;
    }
   
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
  
    return json.toString(); 
  }
  
  
  
}