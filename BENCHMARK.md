# VBD-internet-banking-concept-microservices: synthetic vulnerability benchmark

This fork is a **synthetic, openly labelled benchmark**. It is not the upstream project
and must not be used as one. Each sample is an isolated pair of branches off `baseline`:

- `baseline`: the pinned upstream commit plus any hardening; the common ancestor.
- `smpl/<NNN>/safe`: the genuine negative (validated control) of sample `NNN`.
- `smpl/<NNN>/vuln`: `safe` plus exactly one commit that weakens the control.

The class and the coordinates of every sample live only in `GROUND_TRUTH.csv` on this
orphan branch, never in the analysed branches.

| sample | file | class |
|---|---|---|
| 001 | `internet-banking-api-gateway/src/main/java/com/javatodev/finance/configuration/security/SecurityConfiguration.java` | F345 Security controls bypass or absence - Session Invalidation (debug session-code backdoor) |
| 002 | `core-banking-service/src/main/java/com/javatodev/finance/service/TransactionService.java` | F305 Security controls bypass or absence - Data creation (business limit bypass) |
| 003 | `internet-banking-api-gateway/src/main/java/com/javatodev/finance/configuration/GatewayConfiguration.java` | F115 Security controls bypass or absence (trusted client header for identity) |
