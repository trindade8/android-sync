package br.com.alura.agenda.sinc;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.dto.AlunoSync;
import br.com.alura.agenda.event.AtualizaListaAlunoEvent;
import br.com.alura.agenda.preferences.AlunoPreferences;
import br.com.alura.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoSincronizador {
    private final Context context;
    private EventBus bus = EventBus.getDefault();
    private  AlunoPreferences preferences;

    public AlunoSincronizador(Context context) {

        this.context = context;
        this.preferences = new AlunoPreferences(context);

    }


    public void buscaTodos()
    {
            if (preferences.temVersao())
            {
                BuscaNovosAlunos();
            }
            else
                {
                    BuscaAlunos();
                }
    }

    private void BuscaNovosAlunos() {

        Call<AlunoSync> novos = new RetrofitInicializador()
                                .getAlunoService()
                                .novos(preferences.getVersao());
        buscaAlunosCallBack(novos);

    }

    private void BuscaAlunos() {
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().lista();
        buscaAlunosCallBack(call);
    }

    private void buscaAlunosCallBack(Call<AlunoSync> call) {
        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                String versao = alunoSync.getMomentoDaUltimaModificacao();

                preferences.salvaVersao(versao);

                AlunoDAO dao = new AlunoDAO(context);
                dao.sincroniza(alunoSync.getAlunos());
                dao.close();
                Log.i("versao",preferences.getVersao());
                bus.post(new AtualizaListaAlunoEvent());

            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
                bus.post(new AtualizaListaAlunoEvent());
            }
        });
    }
}