-- This sql script is to be executed when a database with installed jars
-- is renamed. It should be executed from the new database after the rename
-- has been completed.

create dba procedure sqlj.rename_installed_jars()
external name 'informix.jvp.dbapplet.impl.JarHandler.renameInstalledJars'
language java end procedure;

grant execute on procedure sqlj.rename_installed_jars()
	to public as sqlj;

execute procedure sqlj.rename_installed_jars();

drop procedure sqlj.rename_installed_jars();
