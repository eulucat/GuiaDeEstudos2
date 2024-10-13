CENTRO UNIVERSITÁRIO DE BRASÍLIA - UNICEUB
ANÁLISE E DESENVOLVIMENTO DE SISTEMAS
PROGRAMAÇÃO PARA DISPOSITIVOS MÓVEIS
PROFESSOR - ROMES HERIBERTO PIRES DE ARAUJO
ALUNO - LUCAS VASCO DE ARAUJO
RA - 72250217

READ_ME

Descrição do Projeto

O Guia de Estudos é um aplicativo Android nativo desenvolvido com Kotlin, projetado para ajudar os usuários a organizarem seus estudos. Ele permite o gerenciamento de disciplinas e conteúdos a serem estudados, possibilitando o cadastro, visualização, atualização e exclusão (CRUD) de informações. O aplicativo também conta com notificações locais para lembrar os usuários dos estudos pendentes.

Autor

Lucas Vasco de Araujo

Instruções de Instalação

Pré-requisitos

Dispositivo Android com Android 6.0 (API 23) ou superior, ou emulador configurado no Android Studio.

Abra o projeto no Android Studio:

Vá em File > Open e navegue até a pasta do projeto clonado/baixado.

Compile e execute:

Conecte um dispositivo Android ou configure um emulador.

Clique no botão Run (ícone de play) no Android Studio para compilar e executar o aplicativo.

Instalar no dispositivo:

Caso não esteja usando o Android Studio, você pode exportar o APK do projeto navegando em Build > Build Bundle(s) / APK(s) > Build APK(s).

Em seguida, transfira e instale o APK no dispositivo Android.

Funcionalidades Desenvolvidas

1. CRUD Completo com SQLite:

O aplicativo permite que os usuários criem, leiam, atualizem e excluam (CRUD) disciplinas e conteúdos de estudo. Os dados são armazenados localmente utilizando o banco de dados SQLite, garantindo que as informações sejam preservadas mesmo após o fechamento do aplicativo.

2. 4 Telas:

Tela principal: Exibe uma mensagem de boas-vindas e fornece acesso ao gerenciamento de estudos.
Tela de cadastro: Permite ao usuário adicionar novas disciplinas, conteúdos, horário e dia planejados para estudo.
Tela de estudos pendentes: Exibe uma lista das disciplinas cadastradas, permitindo marcar como estudado ou excluir um item.
Tela de estudos realizados: Exibe as disciplinas marcadas como estudadas, permitindo voltar algum item para os estudos pendentes ou até mesmo excluir.

3. Notificações Locais:

O aplicativo envia notificações locais para lembrar o usuário de seus estudos pendentes.

4. Interface de Usuário Intuitiva:

Seguindo o Material Design, o aplicativo possui uma interface simples, fluida e responsiva, facilitando a navegação e o uso por parte dos usuários, através do Jetpack Compose.

Como Usar o Aplicativo Guia de Estudos

Abrindo o Aplicativo:

Quando você abrir o aplicativo, verá a tela principal chamada "Guia de Estudos", com uma mensagem de boas-vindas.

Nesta tela, você verá a opção "Avançar". Esta opção leva você para a próxima tela onde poderá cadastrar suas matérias e conteúdos de estudo.

Cadastrando um Estudo:

No formulário, preencha os seguintes campos:

Nome da Disciplina: Escreva o nome da disciplina que você deseja adicionar (ex.: Matemática, Física, etc.).

Nome do Conteúdo: Coloque o conteúdo ou tópico específico que deseja estudar (ex.: Trigonometria, Termodinâmica, etc.).

Data e Hora: Selecione a data e hora em que você deseja estudar esse conteúdo.

Após preencher os campos, toque no botão "Cadastrar". O estudo será salvo e aparecerá na lista de estudos pendentes na tela principal do aplicativo.

Visualizando os Estudos Pendentes:

Na tela de Estudos Pendentes, você verá a lista de todos os estudos que cadastrou. Cada estudo será exibido com o nome da disciplina, o conteúdo e a data/hora que você selecionou.

Se você já realizou o estudo, pode marcá-lo como concluído tocando no botão "Marcar como estudado" ao lado do estudo. 

Para excluir um estudo, basta tocar no ícone de Excluir no estudo que deseja remover. O aplicativo perguntará se você tem certeza, e, ao confirmar, o estudo será removido da lista.

Acessando a Página de Estudos Realizados:

Na tela principal do aplicativo, onde estão listados os estudos pendentes, você verá um botão chamado "Estudos Realizados". Toque nesse botão para ir diretamente à página que mostra tudo o que você já marcou como concluído.

Ao tocar no botão, a página será exibida com a lista de estudos que foram finalizados. Isso inclui todos os conteúdos que você marcou como "estudado" na lista de estudos pendentes.

Para excluir um estudo, basta tocar no ícone de Excluir no estudo que deseja remover. O aplicativo perguntará se você tem certeza, e, ao confirmar, o estudo será removido da lista.

Caso tenha marcado algum estudo como realizado por engano, basta tocar no ícone de Voltar para Estudos Pendentes.



