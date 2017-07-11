package org.rpereira.robot;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.rpereira.robot.modules.IModule;
import org.rpereira.robot.task.ITask;
import org.rpereira.robot.task.Task;
import org.rpereira.robot.utils.Logger;
import org.rpereira.robot.utils.Timer;

/** the robot */
public final class Robot {

	public static Robot instance;

	/** saving directory holding robot data */
	private final String runDirPath;
	private final File runDir;

	/** hashmap of robot modules */
	private final HashMap<String, IModule> modules;

	/** number of update per seconds */
	private final Timer timer;

	/** registered tasks */
	private final HashMap<Class<? extends ITask>, ITask> tasks;
	private final Stack<Task> taskStack;

	public Robot() {
		this("./");
	}

	public Robot(String dirpath) {
		super();
		instance = this;
		this.timer = new Timer();
		this.runDirPath = dirpath.endsWith(File.separator) ? dirpath : dirpath + File.separator;
		this.runDir = new File(runDirPath);
		this.tasks = new HashMap<Class<? extends ITask>, ITask>();
		this.taskStack = new Stack<Task>();
		this.modules = new HashMap<String, IModule>();
	}

	/** to be called once on pet initialization */
	public final void initialize() {
		Logger.get().log(Logger.Level.FINE, "initializing Robot");
		this.loadModules();
		this.onInitialize();
	}

	private final void loadModules() {
		Logger.get().log(Logger.Level.FINE, "loading Robot modules");
		this.registerModuleDirectories("plugin", "mod", "plugins", "mods", "module", "modules");
		this.onModuleLoaded();
		Logger.get().log(Logger.Level.FINE, "modules loaded");
	}

	/**
	 * load modules in the given directories (relatively to the robot dirpath)
	 */
	public final void registerModuleDirectories(String... dirs) {
		for (String dir : dirs) {
			String dirpath = this.runDirPath + dir;
			this.registerModuleDirectory(new File(dirpath));
		}
	}

	/** find mods into the given folder and try to load it */
	private final void registerModuleDirectory(File directory) {

		if (!directory.exists()) {
			Logger.get().log(Logger.Level.WARNING, "Module folder doesnt exists: " + directory.getAbsolutePath());
			return;
		}

		if (!directory.isDirectory()) {
			Logger.get().log(Logger.Level.WARNING, "Module folder isnt a folder? " + directory.getAbsolutePath());
			return;
		}

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				this.registerModuleDirectory(file);
				continue;
			}

			try {
				this.registerModule(file);
			} catch (InstantiationException e) {
				e.printStackTrace(Logger.get().getPrintStream());
			} catch (IllegalAccessException e) {
				e.printStackTrace(Logger.get().getPrintStream());
			} catch (IOException e) {
				e.printStackTrace(Logger.get().getPrintStream());
			} catch (ClassNotFoundException e) {
				e.printStackTrace(Logger.get().getPrintStream());
			}
		}
	}

	/**
	 * register the given module jar file
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public final void registerModule(File file)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		@SuppressWarnings("resource")
		JarFile jar = new JarFile(file.getAbsolutePath());
		Enumeration<JarEntry> entries = jar.entries();
		URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();

			if (entry.getName().endsWith(".class")) {
				String clazzname = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
				Class<?> moduleClass = cl.loadClass(clazzname);
				this.registerModule(moduleClass);
			}
		}
	}

	/**
	 * register the given module class
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public final void registerModule(Class<?> moduleClass) throws InstantiationException, IllegalAccessException {
		Class<?>[] interfaces = moduleClass.getInterfaces();
		boolean isModule = false;
		for (Class<?> inter : interfaces) {
			if (inter == IModule.class) {
				isModule = true;
				break;
			}
		}

		if (!isModule) {
			return;
		}

		IModule imodule = (IModule) moduleClass.newInstance();
		this.registerModule(imodule);
	}

	/** register a module to the robot */
	public final void registerModule(IModule module) {
		String key = module.getName().toLowerCase();
		if (this.modules.containsKey(key)) {
			Logger.get().log(Logger.Level.ERROR,
					"Tried to register a module with an already-taken name ID (or maybe it was registered twice?)",
					key);
			return;
		}
		Logger.get().log(Logger.Level.FINE, "Module registered", module);
		this.modules.put(key, module);
	}

	/** register a module to the robot */
	public final IModule unregisterModule(String moduleName) {
		if (!this.modules.containsKey(moduleName)) {
			Logger.get().log(Logger.Level.ERROR, "Tried to unregister an unregistered module", moduleName);
			return (null);
		}
		Logger.get().log(Logger.Level.FINE, "Module unregistered", moduleName);
		return (this.modules.remove(moduleName));
	}

	public final IModule unregisterModule(IModule module) {
		return (this.unregisterModule(module.getName()));
	}

	/** get robot run dir */
	public final File getRunDir() {
		return (this.runDir);
	}

	/** get a module by it class name */
	public final IModule getModule(String name) {
		return (this.modules.get(name));
	}

	/** get a module by it class name */
	public IModule getModule(Class<? extends IModule> moduleClass) {
		return (this.getModule(moduleClass.getSimpleName()));
	}

	/** to be called once on pet deinitialization */
	public final void deinitialize() {
		Logger.get().log(Logger.Level.FINE, "deinitializing Robot");
		this.onDeinitialize();
	}

	/** update the robot */
	public final void update() {
		this.timer.update();
		while (!this.taskStack.isEmpty()) {
			Task task = this.taskStack.pop();
			ITask itask = this.tasks.get(task.getITaskClass());
			itask.run(this, task.getArguments());
		}

		for (IModule module : this.modules.values()) {
			module.update(this);
		}

		this.onUpdate();
	}

	public boolean isRunning() {
		return (true);
	}

	/** get this pet timer */
	public final Timer getTimer() {
		return (this.timer);
	}

	/** add a task */
	public final void addTask(Task task) {
		this.taskStack.push(task);
	}

	/** register a new task */
	public final void registerTask(Class<? extends ITask> itask) {
		try {
			this.tasks.put(itask, itask.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** called on robot module loaded */
	protected void onModuleLoaded() {
		// TODO Auto-generated method stub
	}

	/** called on robot initialization */
	protected void onInitialize() {
		// TODO Auto-generated method stub
	}

	/** called on robot deinitialization */
	protected void onDeinitialize() {
		// TODO Auto-generated method stub
	}

	/** called on robot updates */
	protected void onUpdate() {
		// TODO Auto-generated method stub
	}

	/** robot instance */
	public static final Robot instance() {
		return (instance);
	}
}
