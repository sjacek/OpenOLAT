package org.olat.course.nodes.gta.ui;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.components.form.flexible.FormItem;
import org.olat.core.gui.components.form.flexible.FormItemContainer;
import org.olat.core.gui.components.form.flexible.elements.FormLink;
import org.olat.core.gui.components.form.flexible.impl.FormBasicController;
import org.olat.core.gui.components.form.flexible.impl.FormEvent;
import org.olat.core.gui.components.form.flexible.impl.FormLayoutContainer;
import org.olat.core.gui.components.link.Link;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.Event;
import org.olat.core.gui.control.WindowControl;
import org.olat.course.nodes.GTACourseNode;
import org.olat.course.nodes.gta.GTAManager;
import org.olat.course.nodes.gta.Task;
import org.olat.course.run.environment.CourseEnvironment;
import org.olat.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * Initial date: 10 août 2017<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class ConfirmResetTaskController extends FormBasicController {
	
	private FormLink dontResetButton;
	
	private Task task;
	private final GTACourseNode gtaNode;
	private final CourseEnvironment courseEnv;
	
	@Autowired
	private GTAManager gtaManager;
	@Autowired
	private UserManager userManager;
	
	public ConfirmResetTaskController(UserRequest ureq, WindowControl wControl, Task task,
			GTACourseNode gtaNode, CourseEnvironment courseEnv) {
		super(ureq, wControl, "participant_reset_task");
		this.task = task;
		this.gtaNode = gtaNode;
		this.courseEnv = courseEnv;
		initForm(ureq);
	}
	
	public Task getTask() {
		return task;
	}

	@Override
	protected void initForm(FormItemContainer formLayout, Controller listener, UserRequest ureq) {
		if(formLayout instanceof FormLayoutContainer) {
			FormLayoutContainer layoutCont = (FormLayoutContainer)formLayout;
			String allower = userManager.getUserDisplayName(task.getAllowResetIdentity());
			String message = translate("participant.confirm.reset.task.text", new String[] { allower, task.getTaskName() });
			layoutCont.contextPut("msg", message);
		}
		
		FormLayoutContainer buttonsCont = FormLayoutContainer.createButtonLayout("buttons", getTranslator());
		formLayout.add(buttonsCont);
		uifactory.addFormCancelButton("cancel", buttonsCont, ureq, getWindowControl());
		dontResetButton = uifactory.addFormLink("participant.confirm.reset.task.nok", buttonsCont, Link.BUTTON);
		uifactory.addFormSubmitButton("participant.confirm.reset.task.ok", buttonsCont);
	}

	@Override
	protected void doDispose() {
		//
	}

	@Override
	protected void formOK(UserRequest ureq) {
		task = gtaManager.resetTask(task, gtaNode, courseEnv);
		gtaManager.log("Reset task", "reset task", task, getIdentity(), getIdentity(), null, courseEnv, gtaNode);
		fireEvent(ureq, Event.DONE_EVENT);
	}

	@Override
	protected void formInnerEvent(UserRequest ureq, FormItem source, FormEvent event) {
		if(dontResetButton == source) {
			task = gtaManager.resetTaskRefused(task, gtaNode);
			gtaManager.log("Refuse reset task", "refuse reset task", task, getIdentity(), getIdentity(), null, courseEnv, gtaNode);
			fireEvent(ureq, Event.DONE_EVENT);
		}
		super.formInnerEvent(ureq, source, event);
	}

	@Override
	protected void formCancelled(UserRequest ureq) {
		fireEvent(ureq, Event.CANCELLED_EVENT);
	}
}