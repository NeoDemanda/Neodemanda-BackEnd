# ARCHITECTURE.md — Motor de Cálculo NeoDemanda

## 1. Visão geral

O motor de cálculo calcula a demanda normativa de projetos elétricos em edificações de múltiplas unidades consumidoras. Ele recebe os dados do projeto, aplica regras em sequência e devolve o resultado acompanhado da memória de cálculo.

Esta versão contém a estrutura base do motor. As regras normativas serão adicionadas em histórias posteriores.

## 2. Natureza do motor: regras determinísticas

**O motor é baseado exclusivamente em regras explícitas, implementadas como condicionais (IFs) e consultas a faixas (ranges). Não utiliza IA nem aprendizado de máquina.**

A mesma entrada produz sempre a mesma saída, e cada resultado pode ser rastreado até a regra que o originou.

## 3. Estrutura de pacotes

Pacote base: `com.neoenergia.neodemanda.motor`

| Pacote | Responsabilidade |
|---|---|
| `entrada` | Dados de entrada do cálculo |
| `regra` | Contrato das regras de cálculo |
| `contexto` | Estado do cálculo em andamento |
| `saida` | Resultado e memória de cálculo |
| `excecao` | Erros de validação e de execução |
| (raiz) | Orquestrador do fluxo e constantes numéricas |

O motor é Java puro, sem dependência de persistência (JPA), de Spring MVC ou de camada de API.

## 4. Fluxo principal

```mermaid
flowchart LR
    A[Entrada] --> B[Regras] --> C[Saída]
```

1. **Entrada:** dados do projeto, em objeto imutável.
2. **Regras:** o orquestrador executa, em ordem definida, as regras aplicáveis. Cada regra lê os valores já calculados no contexto, produz o seu e registra um passo na memória de cálculo.
3. **Saída:** resultado final acompanhado da memória de cálculo.

## 5. Interface base

`RegraCalculo` é o contrato central. Toda regra possui:

- **identificador** único;
- **referência normativa**;
- **aplicabilidade:** indica se a regra se aplica ao cálculo em andamento;
- **aplicação:** lê do contexto, calcula e registra o resultado.

O orquestrador (`MotorCalculo`) recebe a lista ordenada de regras e as executa. Ele não contém conhecimento normativo, portanto adicionar uma regra não exige alterá-lo.

## 6. Decisões técnicas

| Decisão | Motivo |
|---|---|
| Regras explícitas (IFs e ranges), sem IA/ML | Auditabilidade e previsibilidade do resultado |
| Cada regra como unidade isolada | Rastreabilidade e teste unitário |
| Motor independente de persistência e de web | Testabilidade e baixo acoplamento |
| `BigDecimal` com escala e arredondamento centralizados | Exatidão em valores elétricos |
| Memória de cálculo como parte do resultado | Base para o futuro memorial de cálculo |
