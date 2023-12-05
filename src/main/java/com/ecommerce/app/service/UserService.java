package com.ecommerce.app.service;

import com.ecommerce.app.entities.UserEntity;
import com.ecommerce.app.repository.UserRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String email){
            UserEntity user = userRepository.findByEmail(email);
            if (user == null){
                throw new UsernameNotFoundException(email);
            }
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
            UserDetails userDetails = new User(user.getEmail(), user.getPassword(), roles);
            return userDetails;
        }

        public UserEntity loadUserByEmail(String email) {
            return userRepository.findByEmail(email);
        }

        public Boolean existsByEmail(String email){
            return userRepository.existsByEmail(email);
        };


       public Optional<UserEntity> loadUserById(Long id) {
           return userRepository.findById(id);
       }

        public UserDetails create(UserEntity user){
           var userSaved = userRepository.save(user);
           return loadUserByUsername(userSaved.getEmail());
        }

        public List<UserEntity> getAll(){
            return userRepository.findAll();
        }

        public void delete(Long id) {
            userRepository.deleteById(id);
        }
        public UserEntity update(UserEntity appUser) {
            return userRepository.save(appUser);
        }


    public void generate(HttpServletResponse response) throws DocumentException, IOException {
        var userEntityList = getAll();

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);

        Paragraph paragraph = new Paragraph("Listado de usuarios", fontTiltle);

        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(4);

        table.setWidthPercentage(100f);
        table.setWidths(new int[] { 3, 3, 3, 3 });
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Nombre", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Rol", font));
        table.addCell(cell);

        for (UserEntity userEntity : userEntityList) {
            table.addCell(String.valueOf(userEntity.getId()));
            table.addCell(userEntity.getName());
            table.addCell(userEntity.getEmail());
            table.addCell(userEntity.getRole().toString());
        }

        document.add(table);

        document.close();

    }

}



