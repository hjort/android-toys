package co.hjort.apps.meeting.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MeetingDAO {

	private static final String DATABASE_TABLE = "meetings";

	private static MeetingDAO instance;
	private static SQLiteDatabase db;

	public static MeetingDAO getInstance(Context context) {
		synchronized (MeetingDAO.class) {
			if (instance == null) {
				instance = new MeetingDAO();
				db = new DatabaseManager(context).open();
			}
		}
		return instance;
	}

	private MeetingDAO() {
	}

	/*
	public List<Meeting> buscarPorSecao(int secao) {
		List<Meeting> lista = new ArrayList<Meeting>();
		Cursor cursor = db.query(DATABASE_TABLE, new String[] { "_id", "nome",
				"marcado" }, "secao = " + secao, null, null, null, null);
		while (cursor.moveToNext()) {
			Meeting Meeting = new Meeting();
			Meeting.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
			Meeting.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
			Meeting.setMarcado(cursor.getInt(cursor.getColumnIndexOrThrow("marcado")) > 0);
			Meeting.setSecao(secao);
			lista.add(Meeting);
		}
		cursor.close();
		return lista;
	}

	public int incluir(int secao, String nome) {
		ContentValues values = new ContentValues();
		values.put("nome", nome);
		values.put("secao", secao);
		values.put("marcado", 0);
		return (int) db.insert(DATABASE_TABLE, null, values);
	}

	public boolean excluir(int id) {
		return (db.delete(DATABASE_TABLE, "_id = " + id, null) > 0);
	}

	public boolean marcar(int id, boolean valor) {
		ContentValues values = new ContentValues();
		values.put("marcado", valor ? 1 : 0);
		return (db.update(DATABASE_TABLE, values, "_id = " + id, null) > 0);
	}

	public boolean excluirMeetings(int secao) {
		return (db.delete(DATABASE_TABLE, "secao = " + secao, null) > 0);
	}

	public boolean marcarMeetings(int secao, boolean valor) {
		ContentValues values = new ContentValues();
		values.put("marcado", valor ? 1 : 0);
		return (db.update(DATABASE_TABLE, values, "secao = " + secao, null) > 0);
	}
	*/

}
