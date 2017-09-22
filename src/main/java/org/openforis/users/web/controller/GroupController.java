package org.openforis.users.web.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.openforis.users.exception.NotFoundException;
import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.GroupManager;
import org.openforis.users.manager.GroupManager.SearchParameters;
import org.openforis.users.model.Group;
import org.openforis.users.model.Group.Visibility;
import org.openforis.users.web.JsonTransformer;

import spark.Request;
import spark.Response;
import spark.Route;

public class GroupController extends AbstractController {

	public GroupManager GROUP_MANAGER = EntityManagerFactory.getInstance().getGroupManager();

	public GroupController(JsonTransformer jsonTransformer) {
		super(jsonTransformer);
	}

	public Route findGroups = (Request req, Response rsp) -> {
		SearchParameters params = new SearchParameters();
		params.setName(getStringParam(req, "name"));
		params.setEnabled(getBooleanParam(req, "enabled", true));
		params.setSystemDefined(getBooleanParam(req, "systemDefined"));
		params.setVisibility(getEnumParam(req, "visibility", Visibility.class));
		return GROUP_MANAGER.findAll(params);
	};

	public Route getGroup = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		Group group = GROUP_MANAGER.findById(id);
		if (group!= null) {
			return group;
		} else {
			throw new NotFoundException("Group not found");
		}
	};

	public Route addGroup = (Request req, Response rsp) -> {

		MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
		if (req.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
			req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
		}

		Part name = req.raw().getPart("name");
		Part url = req.raw().getPart("url");
		Part description = req.raw().getPart("description");
		Part systemDefined = req.raw().getPart("systemDefined");
		Part visibilityCode = req.raw().getPart("visibilityCode");
		Part enabled = req.raw().getPart("enabled");

		Group group = new Group();
		group.setName(partToString(name));
		group.setLabel(partToString(name)); // TODO
		group.setUrl(partToString(url));
		group.setDescription(partToString(description));
		if (systemDefined != null) group.setSystemDefined(Boolean.parseBoolean(partToString(systemDefined)));
		if (visibilityCode != null) group.setVisibilityCode(partToString(visibilityCode));
		if (enabled != null) group.setEnabled(Boolean.parseBoolean(partToString(enabled))); else group.setEnabled(true);

        Part logo = req.raw().getPart("logo");
        InputStream input = logo.getInputStream();
        byte[] result = IOUtils.toByteArray(input);
        group.setLogo(result);

		GROUP_MANAGER.save(group);
		return group;
	};

	public Route editGroup = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		Group group = GROUP_MANAGER.findById(id);
		setNotNullParams(req, group);
		GROUP_MANAGER.save(group);
		return group;
	};

	public Route deleteGroup = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		Group group = GROUP_MANAGER.findById(id);
		if (group != null) {
			GROUP_MANAGER.deleteById(id);
		} else {
			throw new NotFoundException("Group not found");
		}
		return rsp;
	};

	private static String partToString(Part part) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream()))) {
			return reader.lines().collect(Collectors.joining("\n"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
