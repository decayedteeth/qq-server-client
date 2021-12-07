package cn.edu.ldu.db.tables;

import cn.edu.ldu.db.beans.Member;
import cn.edu.ldu.db.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MemberManager {  
    //���ر������м�¼
    public static void displayAllRows() throws SQLException{
        String sql="SELECT * FROM MEMBER";
        ResultSet rs=null; //�����
        try (
            Connection conn=DBUtils.getConnection(); 
            Statement st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ){
            rs=st.executeQuery(sql); //���ؽ����
            rs.last();//ָ�뵽���һ����¼
            int nRows=rs.getRow();//���ؼ�¼��
            if (nRows==0) {
                System.out.println("û���ҵ������ѯ�����ļ�¼��\n");
            }else {
                rs.beforeFirst(); //ָ�뵽��һ����¼֮ǰ
                StringBuilder buffer=new StringBuilder(); //��̬�ַ���
                while (rs.next()) { //������¼��
                    buffer.append(rs.getInt("id")).append(",");
                    buffer.append(rs.getString("name")).append(",");
                    buffer.append(rs.getString("password")).append(",");
                    buffer.append(rs.getString("email")).append(",");
                    buffer.append(rs.getString("headimage")).append(",");
                    buffer.append(rs.getTimestamp("time")).append("\n");
                }//end while
                System.out.println(buffer.toString());
            }//end if
        }catch (SQLException ex) {
        }finally {
            if (rs!=null) rs.close();
        }//end try
    }//end displayAllRows
    
    //����id���Ҽ�¼
    public static Member getRowById(int id) throws SQLException {
        String sql="SELECT * FROM member WHERE id=?";
        ResultSet rs=null; //�����
        try (
            Connection conn=DBUtils.getConnection(); 
            PreparedStatement st=conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ){     
            st.setInt(1, id); //���ò���
            rs=st.executeQuery(); //���ؽ����
            if (rs.next()) { //�ҵ�
                Member bean=new Member(); //�����û�����ʵ��
                bean.setId(rs.getInt("id"));
                bean.setName(rs.getString("name"));
                bean.setPassword(rs.getString("password"));
                bean.setEmail(rs.getString("email"));
                bean.setHeadImage(rs.getString("headimage"));
                bean.setTime(rs.getTimestamp("time"));
                return bean; //�����û�����ʵ��
            }else { //û�ҵ�
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }finally {
            if (rs!=null) rs.close();
        }//end try  
    }//end getRowById
    //ע���û�
    public static boolean registerUser(Member bean) throws SQLException{
        if (getRowById(bean.getId())!=null) return false;//����û����ڣ���ע��ʧ��
        String sql="INSERT INTO member (id,name,password,email,headimage,time) VALUES (?,?,?,?,?,?)";
        try (
            Connection conn=DBUtils.getConnection(); 
            PreparedStatement st=conn.prepareStatement(sql);
            ){
            //���ò���
            st.setInt(1,bean.getId()); 
            st.setString(2, bean.getName());
            st.setString(3, bean.getPassword());
            st.setString(4, bean.getEmail());
            st.setString(5, bean.getHeadImage());
            st.setTimestamp(6,bean.getTime());
            int affected=st.executeUpdate();
            return affected==1; //ע��ɹ���ʧ��
        } catch (SQLException ex) {
        	ex.printStackTrace();
            return false;
        }finally {
        }//end try  
    }//end registerUser
    
    public static int lastUser() throws SQLException{
    	String sql = "SELECT * FROM MEMBER";
    	ResultSet rs=null;
    	try{
    		Connection conn=DBUtils.getConnection(); 
            Statement st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs=st.executeQuery(sql); //���ؽ����
            rs.last();//ָ�뵽���һ����¼
            }catch (SQLException ex) {
            	
            }
    	return rs.getInt("id");
    }
    //�û���¼
    public static boolean userLogin(Member bean) throws SQLException {
        String sql="SELECT * FROM member WHERE id=? AND password=?";
        ResultSet rs=null;
        try (
            Connection conn=DBUtils.getConnection(); 
            PreparedStatement st=conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ){
            //���ò���
            st.setInt(1,bean.getId()); 
            st.setString(2, bean.getPassword());
            rs=st.executeQuery(); //���ؽ����
            return rs.next(); //��¼�ɹ���ʧ��
        } catch (SQLException ex) {
            return false;
        }finally {
            if (rs!=null) rs.close();
        }//end try  
    }//end userLogin  
    //�޸��û�
    public static boolean updateUser(Member bean) {      
        String sql="UPDATE member SET name=? , password= ? ,"
            + " email=? , headimage= ? , time= ? WHERE id=?";
        try (
            Connection conn=DBUtils.getConnection(); 
            PreparedStatement st=conn.prepareStatement(sql);
            ){           
            //���ò���
            st.setString(1, bean.getName());
            st.setString(2, bean.getPassword());
            st.setString(3, bean.getEmail());
            st.setString(4,bean.getHeadImage());
            st.setTimestamp(5, bean.getTime());
            st.setInt(6,bean.getId());            
            int affected=st.executeUpdate();
            return affected==1; //�޸ĳɹ���ʧ��
        } catch (SQLException ex) {
            return false;
        }//end try  
    }//end updateUser 
    public static boolean updatePasswrd(Member bean) {      
        String sql="UPDATE member SET password= ? ,"
            + " time= ? WHERE id=?";
        try (
            Connection conn=DBUtils.getConnection(); 
            PreparedStatement st=conn.prepareStatement(sql);
            ){           
            //���ò���
            st.setString(1, bean.getPassword());
            st.setString(2,bean.getHeadImage());
            st.setInt(3,bean.getId());            
            int affected=st.executeUpdate();
            return affected==1; //�޸ĳɹ���ʧ��
        } catch (SQLException ex) {
            return false;
        }//end try  
    }//end updateUser
    public static boolean QueIdandEmail(Member bean) throws SQLException{
    	String sql = "SELECT * FROM MEMBER WHERE id=? AND Email=?";
    	ResultSet rs=null;
    	try (
                Connection conn=DBUtils.getConnection(); 
                PreparedStatement st=conn.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ){
                //���ò���
                st.setInt(1,bean.getId()); 
                st.setString(2, bean.getEmail());
                rs=st.executeQuery(); //���ؽ����
                return rs.next(); //��¼�ɹ���ʧ��
            } catch (SQLException ex) {
                return false;
            }finally {
                if (rs!=null) rs.close();
            }
    }
    //ɾ���û�
    public static boolean deleteUser(int id) {      
        String sql="DELETE FROM member WHERE id=?";
        try (
             Connection conn=DBUtils.getConnection(); 
             PreparedStatement st=conn.prepareStatement(sql);
            ){                        
            st.setInt(1,id);         
            int affected=st.executeUpdate();
            return affected==1; //ɾ���ɹ���ʧ��
        } catch (SQLException ex) {
            return false;
        }//end try  
    }//end updateUser     
}//end class