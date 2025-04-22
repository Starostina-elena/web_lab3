package org.lia.utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("YValidator")
public class YValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return; // Пропускаем валидацию, если значение пустое
        }

        try {
            double y = Double.parseDouble(value.toString());
            if (y < -5 || y > 5) {
                FacesMessage msg = new FacesMessage("Ошибка валидации", "Y должен быть от -5 до 5");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        } catch (NumberFormatException e) {
            FacesMessage msg = new FacesMessage("Ошибка валидации", "Y должен быть числом");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
