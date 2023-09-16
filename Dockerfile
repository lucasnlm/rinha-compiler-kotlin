FROM archlinux:latest

# Change default shell to Bash for better feature support/easier usage
SHELL [ "/bin/bash", "-c" ]
ENV SHELL=/bin/bash

WORKDIR /var/app

COPY . /var/app

RUN pacman -Syyyy && \
    pacman -S --noconfirm gcc rustup git unzip libxcrypt-compat && \
    rustup default stable  && \
    cargo install rinha  && \
    curl -L https://github.com/lucasnlm/rinha-compiler-kotlin/releases/download/1.0.0/rinha-compiler-kotlin-linux-64.zip > kotlin-rinha.zip  && \
    unzip kotlin-rinha.zip  && \
    rm kotlin-rinha.zip && \
    mv rinha-compiler-kotlin.kexe rinhak

ENV PATH="${PATH}:/root/.cargo/bin/"

ENTRYPOINT ["/bin/bash"]
