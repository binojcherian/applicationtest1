<%-- 
    Document   : ViewCentreList
    Created on : Mar 7, 2012, 1:08:46 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="ErrorPage.jsp" import="java.util.*,java.sql.*,Controller.*,java.util.ArrayList,Entity.*,model.*" %>
<%
        Tabulation mark=new Tabulation(request, response);
%>
<div id="CentreList">
                                <select id="Centre" Name="Centre" style="width: 350px">
                                    <option value="-1">.....Select.....</option>
                                    <% ArrayList<Centre> CentreList=mark.getAllCentresForCourse();
                                           int i=0;
                                            int count=CentreList.size();

                                                    while(i<count)
                                                        {
                                                        Centre centre=CentreList.get(i);
                                            %>
                                            <option  value="<%=centre.CentreId %>"><%=centre.CentreName %></option>

                                            <%i++; }
                                            %>
                                </select>
                            </div>
