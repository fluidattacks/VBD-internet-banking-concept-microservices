# Synthetic vulnerability benchmark — NOT FOR PRODUCTION

This repository (`fluidattacks/VBD-internet-banking-concept-microservices`) is a
**fork used exclusively as a labeled vulnerability benchmark**. On the
`benchmark/*` branches, code has been deliberately modified to contain known,
documented weaknesses so that vulnerability detectors (SAST tools, LLM security
agents) can be scored against ground truth.

- Do **not** deploy this code. Do **not** contribute these changes upstream to
  `JavatoDev-com/internet-banking-concept-microservices`.
- Every injected sample is one commit whose message states it is a synthetic,
  deliberately vulnerable benchmark sample. Its parent commit is the clean twin.
- Ground truth is recorded in `GROUND_TRUTH.csv`. Candidates that were examined
  and rejected as "forced" (not a genuine host for the claimed weakness class)
  are recorded in `GROUND_TRUTH_rejected.csv` with the reason.
- Weakness classes follow the Fluid Attacks criteria numbering (F115, F305,
  F345, ...).

Evaluation harnesses should feed detectors the code only and withhold the
manifest files and commit messages, which exist for auditability and scoring.
