package fr.maxlego08.auth.zcore.utils.storage;

public interface Saver {
	
	void save(Persist persist);
	void load(Persist persist);
}
