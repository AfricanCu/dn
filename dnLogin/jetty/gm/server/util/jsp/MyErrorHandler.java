package gm.server.util.jsp;

//
//  ========================================================================
//  Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.AbstractHttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ErrorHandler;

/* ------------------------------------------------------------ */
/**
 * <pre>
 * 全部重写了错误页面处理器，将错误页面的编码改为utf-8 
 * 
 * Handler for Error pages An ErrorHandler is
 * registered with {@link ContextHandler#setErrorHandler(ErrorHandler)} or
 * {@link org.eclipse.jetty.server.Server#addBean(Object)}. It is called by the
 * HttpResponse.sendError method to write a error page.
 * </pre>
 */
public class MyErrorHandler extends ErrorHandler {
	boolean _showStacks = true;
	boolean _showMessageInTitle = true;
	String _cacheControl = "must-revalidate,no-cache,no-store";

	/* ------------------------------------------------------------ */
	/*
	 * @see org.eclipse.jetty.server.server.Handler#handle(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse, int)
	 */
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		AbstractHttpConnection connection = AbstractHttpConnection
				.getCurrentConnection();
		connection.getRequest().setHandled(true);
		String method = request.getMethod();
		if (!method.equals(HttpMethods.GET) && !method.equals(HttpMethods.POST)
				&& !method.equals(HttpMethods.HEAD))
			return;
		response.setContentType("text/html; charset=utf8");
		if (_cacheControl != null)
			response.setHeader(HttpHeaders.CACHE_CONTROL, _cacheControl);
		PrintWriter writer = response.getWriter();
		handleErrorPage(request, writer, connection.getResponse().getStatus(),
				connection.getResponse().getReason());
		writer.flush();
	}

	/* ------------------------------------------------------------ */
	protected void handleErrorPage(HttpServletRequest request, Writer writer,
			int code, String message) throws IOException {
		writeErrorPage(request, writer, code, message, _showStacks);
	}

	/* ------------------------------------------------------------ */
	protected void writeErrorPage(HttpServletRequest request, Writer writer,
			int code, String message, boolean showStacks) throws IOException {
		if (message == null)
			message = HttpStatus.getMessage(code);

		writer.write("<html>\n<head>\n");
		writeErrorPageHead(request, writer, code, message);
		writer.write("</head>\n<body>");
		writeErrorPageBody(request, writer, code, message, showStacks);
		writer.write("\n</body>\n</html>\n");
	}

	/* ------------------------------------------------------------ */
	protected void writeErrorPageHead(HttpServletRequest request,
			Writer writer, int code, String message) throws IOException {
		writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
		writer.write("<title>Error ");
		writer.write(Integer.toString(code));

		if (_showMessageInTitle) {
			writer.write(' ');
			write(writer, message);
		}
		writer.write("</title>\n");
	}

	/* ------------------------------------------------------------ */
	protected void writeErrorPageBody(HttpServletRequest request,
			Writer writer, int code, String message, boolean showStacks)
			throws IOException {
		String uri = request.getRequestURI();

		writeErrorPageMessage(request, writer, code, message, uri);
		if (showStacks)
			writeErrorPageStacks(request, writer);
		writer.write("<hr /><i><small>Powered by Jetty://</small></i>");
		for (int i = 0; i < 20; i++)
			writer.write("<br/>                                                \n");
	}

	/* ------------------------------------------------------------ */
	protected void writeErrorPageMessage(HttpServletRequest request,
			Writer writer, int code, String message, String uri)
			throws IOException {
		writer.write("<h2>HTTP ERROR ");
		writer.write(Integer.toString(code));
		writer.write("</h2>\n<p>Problem accessing ");
		write(writer, uri);
		writer.write(". Reason:\n<pre>    ");
		write(writer, message);
		writer.write("</pre></p>");
	}

	/* ------------------------------------------------------------ */
	protected void writeErrorPageStacks(HttpServletRequest request,
			Writer writer) throws IOException {
		Throwable th = (Throwable) request
				.getAttribute("javax.servlet.error.exception");
		while (th != null) {
			writer.write("<h3>Caused by:</h3><pre>");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			th.printStackTrace(pw);
			pw.flush();
			write(writer, sw.getBuffer().toString());
			writer.write("</pre>\n");

			th = th.getCause();
		}
	}

	/* ------------------------------------------------------------ */
	/**
	 * Get the cacheControl.
	 * 
	 * @return the cacheControl header to set on error responses.
	 */
	public String getCacheControl() {
		return _cacheControl;
	}

	/* ------------------------------------------------------------ */
	/**
	 * Set the cacheControl.
	 * 
	 * @param cacheControl
	 *            the cacheControl header to set on error responses.
	 */
	public void setCacheControl(String cacheControl) {
		_cacheControl = cacheControl;
	}

	/* ------------------------------------------------------------ */
	/**
	 * @return True if stack traces are shown in the error pages
	 */
	public boolean isShowStacks() {
		return _showStacks;
	}

	/* ------------------------------------------------------------ */
	/**
	 * @param showStacks
	 *            True if stack traces are shown in the error pages
	 */
	public void setShowStacks(boolean showStacks) {
		_showStacks = showStacks;
	}

	/* ------------------------------------------------------------ */
	/**
	 * @param showMessageInTitle
	 *            if true, the error message appears in page title
	 */
	public void setShowMessageInTitle(boolean showMessageInTitle) {
		_showMessageInTitle = showMessageInTitle;
	}

	/* ------------------------------------------------------------ */
	public boolean getShowMessageInTitle() {
		return _showMessageInTitle;
	}

	/* ------------------------------------------------------------ */
	protected void write(Writer writer, String string) throws IOException {
		if (string == null)
			return;

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);

			switch (c) {
			case '&':
				writer.write("&amp;");
				break;
			case '<':
				writer.write("&lt;");
				break;
			case '>':
				writer.write("&gt;");
				break;

			default:
				if (Character.isISOControl(c) && !Character.isWhitespace(c))
					writer.write('?');
				else
					writer.write(c);
			}
		}
	}
}
