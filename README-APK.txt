PAULISTA LOGÍSTICA — PROJETO APK DO MOTOBOY

Este projeto é a primeira base nativa Android do aplicativo do motoboy.
Ele abre o Motoboy Web existente dentro de um WebView e recebe notificações
nativas pelo Firebase Cloud Messaging.

ANTES DE COMPILAR:
1. No Firebase Console, registre um aplicativo Android no projeto:
   logistica-paulista
2. Use o pacote:
   com.paulistalogistica.motoboy
3. Baixe o arquivo google-services.json.
4. Coloque o arquivo em:
   app/google-services.json
5. Abra o projeto no Android Studio.
6. Sincronize o Gradle e gere o APK.

IMPORTANTE:
- A Central Web continua em https://logistica-paulista.web.app/
- O Motoboy Web continua em https://logistica-paulista.web.app/motoboy/
- O APK usa o mesmo backend Firebase.
- O som/vibração em segundo plano é nativo Android.
- O Android pode limitar a abertura automática em tela cheia dependendo
  das permissões e configurações do aparelho. A notificação de alta
  prioridade é o mecanismo principal.

O arquivo google-services.json NÃO está incluído porque ele precisa ser
gerado especificamente para o aplicativo Android registrado no Firebase.
