Class | TDD Controller |
|---:|:---|
Responsibility | Loading specific TDD Mode, Setting GUI into appropriate TDD mode, Evaluation of current development stage |
Collaborators | Config, Code, Test, Tracking, TTD Types, GUI, Virtual Kata |

Class | TDD Types |
|---:|:---|
Responsibility | Abstract class with behavior we expect from a test driven mode-type, e.g. list of phases, phase relevant GUI info like a status bar/green lights, timer.. |
Collaborators | TDD Controller, maybe GUI? |

Class | BabySteps |
|---:|:---|
Responsibility | BabySteps specific behavior, timer evaluation |
Collaborators | TDD Controller, TDD Types, Timer, GUI? |

Class | Simple TDD |
|---:|:---|
Responsibility | Most basic TDD implementation |
Collaborators | TDD Controller, TDD Types, GUI? |

Class | ATDD |
|---:|:---|
Responsibility | ATDD specific implementation, logic handling ATDD stages, GUI elements reflecting new stages? |
Collaborators | TDD Controller, TDD Types, GUI? |

Class | Timer |
|---:|:---|
Responsibility | Starting a timer |
Collaborators | BabySteps |