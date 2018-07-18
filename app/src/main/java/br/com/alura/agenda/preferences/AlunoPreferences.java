package br.com.alura.agenda.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.alura.agenda.modelo.Aluno;

public class AlunoPreferences {

    private static final String ALUNO_PREFERENCES = "br.com.alura.agenda.preferences.AlunoPreferences";
    private static final String KEY_VERSAO = "versao_do_dado";
    private final Context Context;

    public AlunoPreferences(Context context)
    {
        this.Context = context;
    }
    public void salvaVersao(String versao) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_VERSAO,versao);
        editor.commit();

    }

    public String getVersao() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(KEY_VERSAO, "");
   }

    private SharedPreferences getSharedPreferences() {
        return this.Context.getSharedPreferences(ALUNO_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean temVersao()
    {
        return !getVersao().isEmpty();
    }
}
