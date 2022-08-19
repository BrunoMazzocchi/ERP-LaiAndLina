package com.laiandlina.crm.web.config;

import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;

@WebServlet("/images/*")
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
        File file = new File("C:\\profilePictures", filename);
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }
}