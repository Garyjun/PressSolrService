package com.channelsoft.appframe.taglib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;


/**
 * Implementation of <code>TagExtraInfo</code> for the <b>constants</b>
 * tag, identifying the scripting object(s) to be made visible.
 *
 * @author Matt Raible
 * @version $Revision$ $Date$
 */
public class ConstantsTei extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {
		String type = data.getAttributeString("type");
		if (type == null)
			type = "java.util.Set";		
		return new VariableInfo[] { new VariableInfo(data
				.getAttributeString("id"), type, true, VariableInfo.AT_END) };

	}
}
