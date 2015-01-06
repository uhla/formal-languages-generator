/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package generator.common.tools;

import generator.core.FormalLanguagesExampleGenerator;
import generator.modules.cfgexamplegenerator.ColorController;
import generator.modules.cfgexamplegenerator.ColorPriority;
import generator.modules.cfgexamplegenerator.cfgtransformationalgorithm.SetList;
import java.awt.Color;
import java.awt.Container;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author drasto
 */
public final class CriteriaConstraintsChecker
{

	private JLabel notice;
	private Action[] toDisable;
	private SpinnersSupport shouldControl;
	private ColorController colors;
	private Map<JSpinner, Set<JSpinner>> errorCount = new HashMap<JSpinner, Set<JSpinner>>();
	private Map<JSpinner, Set<JSpinner>> warningCount = new HashMap<JSpinner, Set<JSpinner>>();
	private List<String> errorMessages = new SetList<String>();
	private List<String> warningMessages = new SetList<String>();
	// private Map<JSpinner, Integer> lowerBounds = new HashMap<JSpinner, Integer>();
	// private Map<JSpinner, Integer> upperBounds = new HashMap<JSpinner, Integer>();
	// private CFGExampleGeneratorView client;
	// private static final ResourceBundle bundle = ResourceBundle.getBundle("CFGExampleGeneratorView", new
	// Locale("en"));
	private static ResourceBundle criteriaBundle = ResourceBundle.getBundle("CriteriaChecker", new Locale("en"));
	// private static final String RECOMMENDATION_COMP = bundle.getString("RECOMMEND_COMP");
	// private static final String RULE_COMP = bundle.getString("RULE_COMP");
	public static final Color ERROR_COLOR = Color.RED;// new Color(253, 101, 0);
	public static final Color WARNING_COLOR = Color.ORANGE;

	public CriteriaConstraintsChecker(JLabel noticeLabel, Action[] toDisable, SpinnersSupport spinSup,
		ColorController colorController)
	{
		if (noticeLabel == null)
		{
			throw new NullPointerException("noticeLabel");
		}
		if (toDisable == null)
		{
			throw new NullPointerException("toDisable");
		}
		if (spinSup == null)
		{
			throw new NullPointerException("spinSup");
		}
		if (colorController == null)
		{
			throw new NullPointerException("colorController");
		}
		// if(client == null){
		// throw new NullPointerException("client");
		// }
		notice = FormalLanguagesExampleGenerator.getStatusLabel();
		this.toDisable = toDisable;
		shouldControl = spinSup;
		colors = colorController;
		// this.client = client;
	}

	public static void changeLocale(Locale newLocale)
	{
		criteriaBundle = ResourceBundle.getBundle("CriteriaChecker", newLocale);
	}

	private static enum MesType
	{
		ERROR, WARNING;
	}

	private void setSpinnerColor(JSpinner lesser)
	{
		if (!errorCount.get(lesser).isEmpty())
		{
			colors.addNewSpinnerProp(lesser, ColorPriority.ERROR);
		}
		else
		{
			colors.removeSpinnerProp(lesser, ColorPriority.ERROR);
		}
		if (!warningCount.get(lesser).isEmpty())
		{
			colors.addNewSpinnerProp(lesser, ColorPriority.WARNING);
		}
		else
		{
			colors.removeSpinnerProp(lesser, ColorPriority.WARNING);
		}
	}

	private void showErrorsAndWarnings(JSpinner lesser, JSpinner greater)
	{
		setLabelShowing();
		setSpinnerColor(lesser);
		setSpinnerColor(greater);
	}

	private void setLabelShowing()
	{
		if (!errorMessages.isEmpty())
		{
			for (Action a : toDisable)
			{
				a.setEnabled(false);
			}
			notice.setForeground(ERROR_COLOR);
			notice.setText(errorMessages.get(0));
		}
		else if (!warningMessages.isEmpty())
		{
			for (Action a : toDisable)
			{
				a.setEnabled(true);
			}
			notice.setForeground(WARNING_COLOR);
			notice.setText(warningMessages.get(0));
		}
		else
		{
			for (Action a : toDisable)
			{
				a.setEnabled(true);
			}
			notice.setForeground(SystemColor.textText);
			notice.setText("");
		}
	}

	private class SpinnerListener extends ComponentAdapter implements ChangeListener, PropertyChangeListener,
		ContainerListener
	{

		private JSpinner spin;
		private int bound;
		private boolean isLower;
		private MesType type;
		private String message;

		public SpinnerListener(JSpinner spin, int bound, boolean isLower, MesType type, String message)
		{
			this.spin = spin;
			this.bound = bound;
			this.isLower = isLower;
			this.type = type;
			this.message = message;
		}

		public void stateChanged(ChangeEvent e)
		{
			controlBounds(spin, bound, isLower, type, message);
		}

		public void propertyChange(PropertyChangeEvent evt)
		{
			controlBounds(spin, bound, isLower, type, message);
		}

		public void componentAdded(ContainerEvent e)
		{
			controlBounds(spin, bound, isLower, type, message);
		}

		public void componentRemoved(ContainerEvent e)
		{
			controlBounds(spin, bound, isLower, type, message);
		}

		@Override
		public void componentHidden(ComponentEvent e)
		{
			controlBounds(spin, bound, isLower, type, message);
		}
		@Override
		public void componentShown(ComponentEvent e)
		{
			controlBounds(spin, bound, isLower, type, message);
		}

	}

	private class CriteriaPaar extends ComponentAdapter implements ChangeListener, PropertyChangeListener,
		ContainerListener
	{

		private JSpinner lesser;
		private JSpinner greater;
		private CriteriaConstraintsChecker.MesType type;
		private String message;

		private CriteriaPaar(JSpinner lesser, JSpinner greater, CriteriaConstraintsChecker.MesType type, String message)
		{
			this.lesser = lesser;
			this.greater = greater;
			this.type = type;
			this.message = message;
		}

		// public JSpinner getGreater()
		// {
		// return greater;
		// }
		//
		// public JSpinner getLesser()
		// {
		// return lesser;
		// }
		//
		// public String getMessage()
		// {
		// return message;
		// }
		//
		// public MesType getType()
		// {
		// return type;
		// }

		public void stateChanged(ChangeEvent e)
		{

			resolveProblemOnSpinner(lesser, greater, type, message);

		}

		public void propertyChange(PropertyChangeEvent evt)
		{

			resolveProblemOnSpinner(lesser, greater, type, message);

		}

		@Override
		public void componentHidden(ComponentEvent e)
		{

			resolveProblemOnSpinner(lesser, greater, type, message);

		}

		@Override
		public void componentShown(ComponentEvent e)
		{

			resolveProblemOnSpinner(lesser, greater, type, message);

		}

		public void componentAdded(ContainerEvent e)
		{

			resolveProblemOnSpinner(lesser, greater, type, message);

		}

		public void componentRemoved(ContainerEvent e)
		{

			resolveProblemOnSpinner(lesser, greater, type, message);

		}

	}

	public void addErrConstraint(JSpinner lesser, JSpinner greater, String message)
	{
		if (lesser == null)
		{
			throw new NullPointerException("lesser");
		}
		if (greater == null)
		{
			throw new NullPointerException("greater");
		}
		if (message == null)
		{
			throw new NullPointerException("message");
		}

		CriteriaPaar listener = new CriteriaPaar(lesser, greater, MesType.ERROR, message);

		lesser.addChangeListener(listener);
		greater.addChangeListener(listener);

		Container current = lesser.getParent();
		while (current != null)
		{
			current.addComponentListener(listener);
			current.addContainerListener(listener);
			current = current.getParent();
		}

		shouldControl.addSpinnerNotUsedListener(lesser, listener);
		shouldControl.addSpinnerNotUsedListener(greater, listener);
	}

	public void addErrorConstraint(JSpinner spin, Integer bottom, boolean isLower, String message)
	{
		if (spin == null)
		{
			throw new NullPointerException("spin");
		}
		if (message == null)
		{

			message = isLower ? criteriaBundle.getString(spin.getName()) + criteriaBundle.getString("atLeast") + " "
				+ bottom : criteriaBundle.getString(spin.getName()) + criteriaBundle.getString("atMost") + " " + bottom;
			message = message + ".";
			notice.setForeground(WARNING_COLOR);
			notice.setText(message);
		}
		if (message.equals(""))
		{
			throw new IllegalArgumentException("message cannot be empty string");
		}

		SpinnerListener listener = new SpinnerListener(spin, bottom, isLower, MesType.ERROR, message);

		spin.addChangeListener(listener);
		shouldControl.addSpinnerNotUsedListener(spin, listener);

		Container current = spin.getParent();
		while (current != null)
		{
			current.addComponentListener(listener);
			current.addContainerListener(listener);
			current = current.getParent();
		}
	}

	public void addErrConstraint(JSpinner lesser, JSpinner greater)
	{
		addErrConstraint(lesser, greater, "");
	}

	private void resolveProblemOnSpinner(JSpinner lesser, JSpinner greater, MesType type, String message)
	{

		int less = (Integer) lesser.getValue();
		int more = (Integer) greater.getValue();
		if (!message.equals(""))
		{
			message = ". " + message;
		}
		String errMess = "";
		String warMess = "";
		if (lesser.getName().contains("Difficulty") || lesser.getName().contains("Length"))
		{
			errMess = criteriaBundle.getString(lesser.getName()) + criteriaBundle.getString("RULE_COMP_FEM") + " "
				+ firstLower(criteriaBundle.getString(greater.getName())) + message;
			warMess = criteriaBundle.getString(lesser.getName()) + criteriaBundle.getString("RECOMMEND_COMP_FEM") + " "
				+ firstLower(criteriaBundle.getString(greater.getName())) + message;
		}
		else
		{
			errMess = criteriaBundle.getString(lesser.getName()) + criteriaBundle.getString("RULE_COMP") + " "
				+ firstLower(criteriaBundle.getString(greater.getName())) + message;
			warMess = criteriaBundle.getString(lesser.getName()) + criteriaBundle.getString("RECOMMEND_COMP") + " "
				+ firstLower(criteriaBundle.getString(greater.getName())) + message;
		}
		
		errMess += ".";
		warMess += ".";

		if (!errorCount.containsKey(lesser))
		{
			errorCount.put(lesser, new HashSet<JSpinner>());
		}
		if (!errorCount.containsKey(greater))
		{
			errorCount.put(greater, new HashSet<JSpinner>());
		}
		if (!warningCount.containsKey(lesser))
		{
			warningCount.put(lesser, new HashSet<JSpinner>());
		}
		if (!warningCount.containsKey(greater))
		{
			warningCount.put(greater, new HashSet<JSpinner>());
		}

		if (less > more && shouldControl.isCriteriaUsed(lesser) && shouldControl.isCriteriaUsed(greater) && less >= 0
			&& more >= 0)
		{
			if (type == MesType.ERROR)
			{
				errorMessages.add(errMess);
				errorCount.get(lesser).add(greater);
				errorCount.get(greater).add(lesser);
			}
			else
			{
				warningMessages.add(warMess);
				warningCount.get(lesser).add(greater);
				warningCount.get(greater).add(lesser);
			}
		}
		else
		{
			if (type == MesType.ERROR)
			{
				errorMessages.remove(errMess);
				errorCount.get(lesser).remove(greater);
				errorCount.get(greater).remove(lesser);
			}
			else
			{
				warningMessages.remove(warMess);
				warningCount.get(lesser).remove(greater);
				warningCount.get(greater).remove(lesser);
			}
		}
		showErrorsAndWarnings(lesser, greater);

	}

	private void controlBounds(JSpinner spin, int bound, boolean isLower, MesType type, String message)
	{

		int value = (Integer) spin.getValue();

		if (!errorCount.containsKey(spin))
		{
			errorCount.put(spin, new HashSet<JSpinner>());
		}

		if (!warningCount.containsKey(spin))
		{
			warningCount.put(spin, new HashSet<JSpinner>());
		}

		boolean notOk;
		if (isLower)
		{
			notOk = value < bound;
		}
		else
		{
			notOk = value > bound;
		}

		if (notOk && shouldControl.isCriteriaUsed(spin))
		{
			if (type == MesType.ERROR)
			{
				errorMessages.add(message);
				errorCount.get(spin).add(spin);
			}
			else
			{
				warningMessages.add(message);
				warningCount.get(spin).add(spin);
			}
		}
		else
		{
			if (type == MesType.ERROR)
			{
				errorMessages.remove(message);
				errorCount.get(spin).remove(spin);
			}
			else
			{
				warningMessages.remove(message);
				warningCount.get(spin).remove(spin);
			}
		}

		setLabelShowing();
		setSpinnerColor(spin);
	}

	private String firstLower(String origin)
	{
		if (origin == null)
		{
			throw new NullPointerException("origin");
		}
		if (origin.equals(""))
		{
			throw new IllegalArgumentException("origin must have at least one character");
		}

		return origin.substring(0, 2).toLowerCase() + origin.substring(2);
	}

}
