package br.com.alura.agenda.firebase;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.dto.AlunoSync;
import br.com.alura.agenda.event.AtualizaListaAlunoEvent;
import br.com.alura.agenda.modelo.Aluno;

public class AgendaMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> mensagemRecebida = remoteMessage.getData();
        Log.i("Mensagem recebida",String.valueOf(mensagemRecebida));
        converteParaAluno(mensagemRecebida);




    }

    private void converteParaAluno(Map<String, String> mensagem) {
        String chaveAcesso = "alunoSync";
        if(mensagem.containsKey(chaveAcesso))
        {
            String  json = mensagem.get(chaveAcesso);
            ObjectMapper mapper = new ObjectMapper();
            try {
                AlunoSync alunoSync = mapper.readValue(json, AlunoSync.class);
                AlunoDAO DAO = new AlunoDAO(this);
                DAO.sincroniza(alunoSync.getAlunos());
                DAO.close();
                EventBus eventBus = EventBus.getDefault();
                eventBus.post(new AtualizaListaAlunoEvent());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
